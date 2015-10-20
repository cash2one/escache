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
		// 获取DB名称
		List<String> mongoDBNames = GaUtils.getMongDBName(scale, dates);

		// 分组将数据插入redis中
		for (String mongoDBName : mongoDBNames) {
			List<DBObject> dbObjects = groupAnalyticsDao.queryGaData(
					mongoDBName, type);
			String[] ids = GaUtils.getIds(dbObjects);
			// 分组将ID保存进入redis
			jedis.del(mongoDBName);
			if (ids != null && ids.length > 0) {
				jedis.sadd(mongoDBName, ids);
			}
		}

		// 进行分组统计
		for (int i = 0; i < mongoDBNames.size() - 1; i++) {
			String mongoDBName = mongoDBNames.get(i);
			// 初始化当天数据
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(GaUtils.displayDate(dates.get(i),scale));
			int size = jedis.smembers(mongoDBName) == null ? 0 : jedis
					.smembers(mongoDBName).size();
			trData.setData(String.valueOf(size));
			trData.setUserNumber(String.valueOf(size));
			result.add(trData);

			// 记录同类群组数据
			for (int j = i + 1; j < dates.size(); j++) {
				GaResultTdData tdData = new GaResultTdData();
				Set<String> sinter = jedis.sinter(mongoDBName,
						mongoDBNames.get(j));
				tdData.setData(String.valueOf(sinter.size()));
				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return calculateTotal(result, GaConstant.VISITOR);
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
		// 分组将数据插入redis中
		Map<String, List<DBObject>> dbObjects = new HashMap<String, List<DBObject>>();

		for (String mongoDBName : mongoDBNames) {
			List<DBObject> totalUser = groupAnalyticsDao.queryGaData(
					mongoDBName, type);

			List<DBObject> newUser = groupAnalyticsDao.queryGaData(mongoDBName,
					type, "0");

			String[] totalIds = GaUtils.getIds(totalUser);
			String[] newUserIds = GaUtils.getIds(newUser);

			// 分组将所有用户保存进入redis
			jedis.del(mongoDBName);
			if (totalIds != null && totalIds.length > 0) {
				jedis.sadd(mongoDBName, totalIds);
			}
			// 分组将新用户保存进入redis
			jedis.del(GaConstant.NEW_USER + mongoDBName);
			if (newUserIds != null && newUserIds.length > 0) {
				jedis.sadd(GaConstant.NEW_USER + mongoDBName, newUserIds);
			}

			dbObjects.put(mongoDBName, totalUser);
			dbObjects.put(GaConstant.NEW_USER + mongoDBName, newUser);

		}

		// 进行分组统计
		for (int i = 0; i < mongoDBNames.size() - 1; i++) {

			String mongoDBName = mongoDBNames.get(i);
			// 初始化数据统计
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(GaUtils.displayDate(dates.get(i),scale));
			trData.setTitle(scale);

			int loginNew = dbObjects.get(GaConstant.NEW_USER + mongoDBName)
					.size();
			int loginTotal = dbObjects.get(mongoDBName).size();

			// 当天留存率=新增用户数/登录用户数*100%
			trData.setData(GaUtils.calculateRetentionRate(loginNew, loginTotal));

			int size = jedis.smembers(mongoDBName) == null ? 0 : jedis
					.smembers(mongoDBName).size();

			trData.setUserNumber(String.valueOf(size));
			result.add(trData);

			// 分组统计
			for (int j = i + 1; j < mongoDBNames.size(); j++) {

				GaResultTdData tdData = new GaResultTdData();

				Set<String> sinter = jedis.sinter(GaConstant.NEW_USER
						+ mongoDBName, mongoDBNames.get(j));
				// N天后留存率 = （新增用户中，在往后的第N天还有登录的用户数）/新增总用户数
				tdData.setData(GaUtils.calculateRetentionRate(sinter.size(),
						loginNew));
				tdData.setValue(GaUtils.calculateRetentionRateValue(
						sinter.size(), loginNew));

				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return calculateTotal(result, GaConstant.RETENTION_RATE);
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
			trData.setCode(GaUtils.displayDate(dates.get(i),scale));
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

		return calculateTotal(result, GaConstant.PV);
	}

	private GaResult calculateTotal(List<GaResultTrData> data, String indicator)
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
			double dayDateRetention = 0.00;

			userNumber += Integer.valueOf(gaResultTrData.getUserNumber());

			if (!indicator.equals(GaConstant.RETENTION_RATE)) {
				initialDate += Integer.valueOf(gaResultTrData.getData());
			}

			int size = gaResultTrData.getGaResultTdDatas().size();

			for (int i = 0; i < size; i++) {

				if (!indicator.equals(GaConstant.RETENTION_RATE)) {
					int value = Integer.valueOf(data.get(i)
							.getGaResultTdDatas().get(count).getData());
					min = value < min ? value : min;
					max = value > max ? value : max;

					dayDate += value;

				} else {
					double value = GaUtils.changeString(data.get(i)
							.getGaResultTdDatas().get(count).getData());
					max = value > max ? value : max;
					min = value < min ? value : min;
					dayDateRetention += value;
				}
			}

			if (!indicator.equals(GaConstant.RETENTION_RATE)) {
				gaResultTdData.setData(String.valueOf(dayDate));

			} else {
				gaResultTdData.setData(String.valueOf(GaUtils
						.calculateRetentionRate(dayDateRetention, size)));

			}

			gaResultTdDatas.add(gaResultTdData);

			count++;
		}

		total.setCode("所有会话");
		total.setUserNumber(String.valueOf(userNumber));

		if (!indicator.equals(GaConstant.RETENTION_RATE)) {
			total.setData(String.valueOf(initialDate));
		} else {
			total.setData("100%");
		}

		total.setGaResultTdDatas(gaResultTdDatas);

		result.add(total);
		result.addAll(data);

		gaResult.setIntervalValue(max - min);
		gaResult.setGaResultTrData(result);

		return gaResult;

	}

}
