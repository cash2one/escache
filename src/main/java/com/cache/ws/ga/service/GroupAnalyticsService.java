package com.cache.ws.ga.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.cache.ws.ga.dao.GroupAnalyticsDao;
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
	 */
	public List<GaResultTrData> queryVisitors(String type, String scale,
			int dateRange) {
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
		for (int i = 0; i < dates.size(); i++) {
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
				tdData.setTitle(scale + "-" + count);
				tdData.setData(String.valueOf(sinter.size()));
				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return result;
	}

	/**
	 * 查询留存率
	 * 
	 * @return
	 */
	public List<GaResultTrData> queryRetentionRate(String type, String scale,
			int dateRange) {
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		Jedis jedis = RedisConfiguration.getInstance().getClient();
		// 获取查询日期
		List<String> dates = GaDateUtils.getQueryDates(scale, dateRange);

		// 分组将数据插入redis中
		Map<String, List<DBObject>> dbObjects = new HashMap<String, List<DBObject>>();
		for (String date : dates) {
			List<DBObject> totalUser = groupAnalyticsDao.queryGaData("ga-"
					+ date, type);
			
			List<DBObject> newUser = groupAnalyticsDao.queryGaData("ga-"
					+ date, type,"0");
			
			
			String[] totalIds = GaUtils.getIds(totalUser);
			String[] newUserIds = GaUtils.getIds(newUser);
			
			// 分组将所有用户保存进入redis
			if (totalIds != null && totalIds.length > 0) {
				jedis.sadd(date, totalIds);
			}
			// 分组将新用户保存进入redis
			if (newUserIds != null && newUserIds.length > 0) {
				jedis.sadd("new-"+date, newUserIds);
			}
			
			dbObjects.put(date, totalUser);
			dbObjects.put("new-"+date, newUser);
			
		}

		// 进行分组统计
		for (int i = 0; i < dates.size(); i++) {
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
			int loginNew = dbObjects.get("new-"+dates.get(i)).size();
			int loginTotal = dbObjects.get(dates.get(i)).size();
			
			trData.setData(GaUtils.calculateRetentionRate(loginNew,loginTotal));

			trData.setUserNumber(String.valueOf(jedis.smembers(dates.get(i))
					.size()));
			result.add(trData);

			// 计算同类群主-留存率
			for (int j = i + 1; j < dates.size(); j++) {
				count++;
				GaResultTdData tdData = new GaResultTdData();
				//（新增用户中，在往后的第N天还有登录的用户数）/新增总用户数
				Set<String> sinter = jedis.sinter("new-"+dates.get(i), dates.get(j));

				tdData.setData(GaUtils.calculateRetentionRate(sinter.size(),loginNew));
				
				tdData.setTitle(scale + "-" + count);

				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return result;
	}

	/**
	 * 查询浏览量
	 * 
	 * @return
	 */
	public List<GaResultTrData> queryPV(String type, String scale,
			int dateRange) {
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		Jedis jedis = RedisConfiguration.getInstance().getClient();
		// 获取查询日期
		List<String> dates = GaDateUtils.getQueryDates(scale, dateRange);
		ConcurrentHashMap<String,Integer> pvDataMap = new ConcurrentHashMap<String,Integer> ();
		// 分组将数据插入redis中
		for (String date : dates) {
			List<DBObject> dbObjects = groupAnalyticsDao.queryGaData("ga-"
					+ date, type);
			String[] ids = GaUtils.getIds(dbObjects);
			//保存PV数据
			pvDataMap.putAll(GaUtils.getPvMap(dbObjects)); 
			// 分组将ID保存进入redis
			if (ids != null && ids.length > 0) {
				jedis.sadd(date, ids);
			}
		}

		// 进行分组统计
		for (int i = 0; i < dates.size(); i++) {
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
				tdData.setTitle(scale + "-" + count);
				tdData.setData(GaUtils.getPv(pvDataMap,sinter));
				trData.getGaResultTdDatas().add(tdData);
			}

		}

		return result;
	}

}
