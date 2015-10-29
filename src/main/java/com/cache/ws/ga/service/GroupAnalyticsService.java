package com.cache.ws.ga.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.cache.ws.constant.GaConstant;
import com.cache.ws.ga.dao.GroupAnalyticsDao;
import com.cache.ws.ga.dto.GaResult;
import com.cache.ws.ga.dto.GaResultTdData;
import com.cache.ws.ga.dto.GaResultTrData;
import com.cache.ws.redis.RedisConfiguration;
import com.cache.ws.util.GaDateUtils;
import com.cache.ws.util.GaUtils;
import com.mongodb.DBObject;

@Component
public class GroupAnalyticsService {

	@Autowired
	private GroupAnalyticsDao groupAnalyticsDao;

	/**
	 * 查询用户数
	 * 
	 * @return
	 * @throws ParseException
	 */
	public GaResult queryVisitors(String type, String scale, int dateRange)
			throws ParseException {
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		Jedis jedis = RedisConfiguration.getInstance().getClient();
		// 获取查询日期
		List<String> dates = GaDateUtils.getQueryDates(scale, dateRange);
		// DB名称集合
		List<String> mongoDBNames = GaUtils.getMongDBName(scale, dates);
		// redis名称集合
		List<String> redisNames = new ArrayList<String>();

		// 分组将数据插入redis中
		for (String mongoDBName : mongoDBNames) {
			List<DBObject> dbObjects = groupAnalyticsDao.queryGaData(
					mongoDBName, type);
			String[] ids = GaUtils.getIds(dbObjects);
			// redis名称
			String redisName = GaUtils.getRedisName(mongoDBName, type);
			redisNames.add(redisName);
			if (ids != null && ids.length > 0) {
				jedis.sadd(redisName, ids);
			}
		}

		// 进行分组统计
		for (int i = 0; i < redisNames.size() - 1; i++) {
			String redisName = redisNames.get(i);
			// 初始化当天数据
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(GaUtils.displayDate(dates.get(i), scale));
			int size = jedis.smembers(redisName) == null ? 0 : jedis.smembers(
					redisName).size();
			trData.setData(String.valueOf(size));
			trData.setUserNumber(String.valueOf(size));
			result.add(trData);

			// 记录同类群组数据
			for (int j = i + 1; j < dates.size(); j++) {
				GaResultTdData tdData = new GaResultTdData();
				Set<String> sinter = jedis.sinter(redisName, redisNames.get(j));
				tdData.setData(String.valueOf(sinter.size()));
				trData.getGaResultTdDatas().add(tdData);
			}

		}

		// 清空redis
		for (String redisName : redisNames) {
			jedis.del(redisName);
		}

		return calculateTotal(result);
	}

	/**
	 * 查询留存率
	 * 
	 * @return
	 * @throws ParseException
	 */
	public GaResult queryRetentionRate(String type, String scale, int dateRange)
			throws ParseException {
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		Jedis jedis = RedisConfiguration.getInstance().getClient();
		// 获取查询日期
		List<String> dates = GaDateUtils.getQueryDates(scale, dateRange);
		// 获取DB名称
		List<String> mongoDBNames = GaUtils.getMongDBName(scale, dates);
		// redis名称集合
		List<String> redisNames = new ArrayList<String>();
		// redis名称集合-新用户
		List<String> newUserRedisNames = new ArrayList<String>();

		// 分组将数据插入redis中
		Map<String, List<DBObject>> dbObjects = new HashMap<String, List<DBObject>>();

		for (String mongoDBName : mongoDBNames) {
			List<DBObject> totalUser = groupAnalyticsDao.queryGaData(
					mongoDBName, type);

			List<DBObject> newUser = groupAnalyticsDao.queryGaData(mongoDBName,
					type, GaConstant.NEW_USER_CODE);

			String[] totalIds = GaUtils.getIds(totalUser);
			String[] newUserIds = GaUtils.getIds(newUser);

			// redis名称
			String redisName = GaUtils.getRedisName(mongoDBName, type);
			redisNames.add(redisName);
			if (totalIds != null && totalIds.length > 0) {
				jedis.sadd(redisName, totalIds);
			}
			// 分组将新用户保存进入redis
			String newUserRedisName = GaConstant.NEW_USER
					+ GaUtils.getRedisName(mongoDBName, type);
			newUserRedisNames.add(newUserRedisName);

			if (newUserIds != null && newUserIds.length > 0) {
				jedis.sadd(newUserRedisName, newUserIds);
			}

			dbObjects.put(redisName, totalUser);
			dbObjects.put(newUserRedisName, newUser);

		}

		// 用于计算总共留存率
		List<List<Integer>> totalData = new ArrayList<List<Integer>>();

		// 进行分组统计
		for (int i = 0; i < mongoDBNames.size() - 1; i++) {

			List<Integer> totalTrData = new ArrayList<Integer>();

			String redisName = redisNames.get(i);
			String newUserRedisName = newUserRedisNames.get(i);
			// 初始化数据统计
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(GaUtils.displayDate(dates.get(i), scale));
			trData.setTitle(scale);

			int loginNew = dbObjects.get(newUserRedisName).size();
			// int loginTotal = dbObjects.get(redisName).size();
			totalTrData.add(loginNew);

			// 当天留存率=新增用户数/登录用户数*100%
			// trData.setData(GaUtils.calculateRetentionRate(loginNew,
			// loginTotal));

			// trData.setValue(GaUtils.calculateRetentionRateValue(loginNew,
			// loginTotal));
			trData.setData("100.00%");
			trData.setValue(100.00);

			int size = jedis.smembers(redisName) == null ? 0 : jedis.smembers(
					redisName).size();

			trData.setUserNumber(String.valueOf(size));
			result.add(trData);

			// 分组统计
			for (int j = i + 1; j < mongoDBNames.size(); j++) {

				GaResultTdData tdData = new GaResultTdData();

				Set<String> sinter = jedis.sinter(newUserRedisNames.get(j),
						redisName);
				totalTrData.add(sinter.size());
				// N天后留存率 = （新增用户中，在往后的第N天还有登录的用户数）/新增总用户数
				tdData.setData(GaUtils.calculateRetentionRate(sinter.size(),
						loginNew));
				tdData.setValue(GaUtils.calculateRetentionRateValue(
						sinter.size(), loginNew));

				trData.getGaResultTdDatas().add(tdData);
			}

			totalData.add(totalTrData);

		}

		// 清空redis
		for (int i = 0; i < mongoDBNames.size(); i++) {
			jedis.del(redisNames.get(i));
			jedis.del(newUserRedisNames.get(i));
		}

		return calculateRetentionRateTotal(result, totalData);
	}

	/**
	 * 查询浏览量
	 * 
	 * @return
	 * @throws ParseException
	 */
	public GaResult queryPV(String type, String scale, int dateRange)
			throws ParseException {
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		Jedis jedis = RedisConfiguration.getInstance().getClient();
		// 获取查询日期
		List<String> dates = GaDateUtils.getQueryDates(scale, dateRange);
		// 获取DB名称
		List<String> mongoDBNames = GaUtils.getMongDBName(scale, dates);

		ConcurrentHashMap<String, Integer> pvDataMap = new ConcurrentHashMap<String, Integer>();
		// 分组将数据插入redis中
		for (String mongoDBName : mongoDBNames) {
			List<DBObject> dbObjects = groupAnalyticsDao.queryGaData(
					mongoDBName, type);
			String[] ids = GaUtils.getIds(dbObjects);
			// 保存PV数据
			pvDataMap.putAll(GaUtils.getPvMap(dbObjects));
			// 分组将ID保存进入redis
			jedis.del(mongoDBName);
			if (ids != null && ids.length > 0) {
				jedis.sadd(mongoDBName, ids);
			}
		}
		// 进行分组统计
		for (int i = 0; i < mongoDBNames.size() - 1; i++) {

			String mongoDBName = mongoDBNames.get(i);
			// 初始化数据统计
			int count = 0;
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(GaUtils.displayDate(dates.get(i), scale));
			trData.setTitle(scale + "-" + count);

			trData.setData(GaUtils.getPv(pvDataMap, jedis.smembers(mongoDBName)));
			int size = jedis.smembers(mongoDBName) == null ? 0 : jedis
					.smembers(mongoDBName).size();

			trData.setUserNumber(String.valueOf(size));
			result.add(trData);

			// 记录同类群组数据
			for (int j = i + 1; j < mongoDBNames.size(); j++) {
				count++;
				GaResultTdData tdData = new GaResultTdData();
				Set<String> sinter = jedis.sinter(mongoDBName,
						mongoDBNames.get(j));
				tdData.setData(GaUtils.getPv(pvDataMap, sinter));
				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return calculateTotal(result);
	}

	private GaResult calculateTotal(List<GaResultTrData> data)
			throws ParseException {
		GaResult gaResult = new GaResult();
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		GaResultTrData total = new GaResultTrData();
		// 用户数
		Integer userNumber = 0;
		// 第0天的数据
		Integer initialDate = 0;

		// 同类群组天数
		Integer count = 0;
		// 最大值（用于区间计算）
		Double max = 0.00;
		// 最小值（用于区间计算）
		Double min = 0.00;

		List<GaResultTdData> gaResultTdDatas = new ArrayList<GaResultTdData>();

		for (GaResultTrData gaResultTrData : data) {
			GaResultTdData gaResultTdData = new GaResultTdData();

			int dayDate = 0;

			userNumber += Integer.valueOf(gaResultTrData.getUserNumber());

			initialDate += Integer.valueOf(gaResultTrData.getData());

			int size = gaResultTrData.getGaResultTdDatas().size();

			for (int i = 0; i < size; i++) {

				int value = Integer.valueOf(data.get(i).getGaResultTdDatas()
						.get(count).getData());
				min = value < min ? value : min;
				max = value > max ? value : max;

				dayDate += value;

			}

			gaResultTdData.setData(String.valueOf(dayDate));

			gaResultTdDatas.add(gaResultTdData);

			count++;
		}

		total.setCode("所有会话");
		total.setUserNumber(String.valueOf(userNumber));

		total.setData(String.valueOf(initialDate));

		total.setGaResultTdDatas(gaResultTdDatas);

		result.add(total);
		result.addAll(data);

		gaResult.setMax(max);
		gaResult.setMin(min);
		gaResult.setGaResultTrData(result);

		return gaResult;

	}

	private GaResult calculateRetentionRateTotal(List<GaResultTrData> data,
			List<List<Integer>> totalData) throws ParseException {
		GaResult gaResult = new GaResult();
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		GaResultTrData total = new GaResultTrData();
		// 用户数
		Integer userNumber = 0;

		// 同类群组天数
		Integer counter = 0;
		// 最大值（用于区间计算）
		Double max = 0.00;
		// 最小值（用于区间计算）
		Double min = 0.00;

		List<GaResultTdData> gaResultTdDatas = new ArrayList<GaResultTdData>();

		// 循环行
		for (GaResultTrData gaResultTrData : data) {
			GaResultTdData gaResultTdData = new GaResultTdData();

			userNumber += Integer.valueOf(gaResultTrData.getUserNumber());

			int size = gaResultTrData.getGaResultTdDatas().size();

			// 循环列
			for (int i = 0; i < size; i++) {

				double value = data.get(i).getGaResultTdDatas().get(counter)
						.getValue();
				max = value > max ? value : max;
				min = value < min ? value : min;

			}
			// 计算总共的留存率
			String retentionRate = calculateTotalRetentionRate(totalData,
					data.size() - counter, counter + 1);

			gaResultTdData.setData(retentionRate);

			gaResultTdDatas.add(gaResultTdData);

			counter++;
		}

		total.setCode("所有会话");
		total.setUserNumber(String.valueOf(userNumber));

		total.setData("100.00%");

		total.setGaResultTdDatas(gaResultTdDatas);

		result.add(total);
		result.addAll(data);

		gaResult.setMax(max);
		gaResult.setMin(min);
		gaResult.setGaResultTrData(result);

		return gaResult;

	}

	private String calculateTotalRetentionRate(List<List<Integer>> totalData,
			int tr, int td) {
		int total = 0;
		int current = 0;

		for (int i = 0; i < tr; i++) {

			total += totalData.get(i).get(0);

			current += totalData.get(i).get(td);

		}

		return GaUtils.calculateRetentionRate(current, total);
	}

}
