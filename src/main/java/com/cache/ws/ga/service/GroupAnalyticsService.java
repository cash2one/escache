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

		// 分组将数据插入redis中
		for (String date : dates) {
			List<DBObject> dbObjects = groupAnalyticsDao.queryGaData("ga-"
					+ date, type);
			String[] ids = GaUtils.getIds(dbObjects);
			// 分组将ID保存进入redis
			if (ids != null && ids.length > 0) {
				jedis.sadd(date, ids);
			}
		}

		// 进行分组统计
		for (int i = 0; i < dates.size() - 1; i++) {
			if (jedis.smembers(dates.get(i)) == null
					|| jedis.smembers(dates.get(i)).size() <= 0) {
				break;
			}
			// 初始化数据
			int count = 0;
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(dates.get(i));
			trData.setTitle(scale + "-" + count);
			trData.setData(String.valueOf(jedis.smembers(dates.get(i)).size()));
			trData.setUserNumber(String.valueOf(jedis.smembers(dates.get(i))
					.size()));
			result.add(trData);

			// 记录同类群组数据
			for (int j = i + 1; j < dates.size(); j++) {
				count++;
				GaResultTdData tdData = new GaResultTdData();
				Set<String> sinter = jedis.sinter(dates.get(i), dates.get(j));
				//tdData.setTitle(scale + "-" + count);
				tdData.setData(String.valueOf(sinter.size()));
				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return calculateTotal(result, "");
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

		// 分组将数据插入redis中
		Map<String, List<DBObject>> dbObjects = new HashMap<String, List<DBObject>>();
		for (String date : dates) {
			List<DBObject> totalUser = groupAnalyticsDao.queryGaData("ga-"
					+ date, type);

			List<DBObject> newUser = groupAnalyticsDao.queryGaData(
					"ga-" + date, type, "0");

			String[] totalIds = GaUtils.getIds(totalUser);
			String[] newUserIds = GaUtils.getIds(newUser);

			// 分组将所有用户保存进入redis
			if (totalIds != null && totalIds.length > 0) {
				jedis.sadd(date, totalIds);
			}
			// 分组将新用户保存进入redis
			if (newUserIds != null && newUserIds.length > 0) {
				jedis.sadd("new-" + date, newUserIds);
			}

			dbObjects.put(date, totalUser);
			dbObjects.put("new-" + date, newUser);

		}

		// 进行分组统计
		for (int i = 0; i < dates.size() - 1; i++) {
			if (jedis.smembers(dates.get(i)) == null
					|| jedis.smembers(dates.get(i)).size() <= 0) {
				break;
			}
			// 初始化数据统计
			int count = 0;
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(dates.get(i));
			trData.setTitle(scale + "-" + count);
			// 留存率=新增用户数/登录用户数*100%（一般统计周期为天）
			int loginNew = dbObjects.get("new-" + dates.get(i)).size();
			// int loginTotal = dbObjects.get(dates.get(i)).size();
			// trData.setData(GaUtils.calculateRetentionRate(loginNew,
			// loginTotal));
			trData.setData("100%");

			trData.setUserNumber(String.valueOf(jedis.smembers(dates.get(i))
					.size()));
			result.add(trData);

			// 计算同类群主-留存率
			for (int j = i + 1; j < dates.size(); j++) {
				count++;
				GaResultTdData tdData = new GaResultTdData();
				// （新增用户中，在往后的第N天还有登录的用户数）/新增总用户数
				Set<String> sinter = jedis.sinter("new-" + dates.get(i),
						dates.get(j));

				tdData.setData(GaUtils.calculateRetentionRate(sinter.size(),
						loginNew));
				tdData.setValue(GaUtils.calculateRetentionRateValue(sinter.size(),
						loginNew));

				//tdData.setTitle(scale + "-" + count);

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
		ConcurrentHashMap<String, Integer> pvDataMap = new ConcurrentHashMap<String, Integer>();
		// 分组将数据插入redis中
		for (String date : dates) {
			List<DBObject> dbObjects = groupAnalyticsDao.queryGaData("ga-"
					+ date, type);
			String[] ids = GaUtils.getIds(dbObjects);
			// 保存PV数据
			pvDataMap.putAll(GaUtils.getPvMap(dbObjects));
			// 分组将ID保存进入redis
			if (ids != null && ids.length > 0) {
				jedis.sadd(date, ids);
			}
		}
		// 进行分组统计
		for (int i = 0; i < dates.size() - 1; i++) {
			if (jedis.smembers(dates.get(i)) == null
					|| jedis.smembers(dates.get(i)).size() <= 0) {
				break;
			}
			// 初始化数据统计
			int count = 0;
			GaResultTrData trData = new GaResultTrData();
			trData.setGaResultTdDatas(new ArrayList<GaResultTdData>());
			trData.setCode(dates.get(i));
			trData.setTitle(scale + "-" + count);

			trData.setData(GaUtils.getPv(pvDataMap,jedis.smembers(dates.get(i))));

			trData.setUserNumber(String.valueOf(jedis.smembers(dates.get(i))
					.size()));
			result.add(trData);

			// 记录同类群组数据
			for (int j = i + 1; j < dates.size(); j++) {
				count++;
				GaResultTdData tdData = new GaResultTdData();
				Set<String> sinter = jedis.sinter(dates.get(i), dates.get(j));
				//tdData.setTitle(scale + "-" + count);
				tdData.setData(GaUtils.getPv(pvDataMap, sinter));
				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return calculateTotal(result, "");
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
					int date = Integer.valueOf(data.get(i).getGaResultTdDatas()
							.get(count).getData());
					max = date > max ? date : max;
					dayDate += date;

				} else {
					double date = GaUtils.changeString(data.get(i)
							.getGaResultTdDatas().get(count).getData());
					max = date > max ? date : max;
					dayDateRetention += date;
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

		gaResult.setMax(max);
		gaResult.setGaResultTrData(result);

		return gaResult;

	}

}
