package com.cache.ws.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.cache.ws.es.dto.IndicatorData;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Component
public final class MongoDBOperate {

	/**
	 * 在MongoDB中判断数据是否存在
	 * 
	 * @param type
	 *            数据type
	 * @param collection
	 *            集合,表
	 * 
	 * @return
	 */
	public boolean isMongoDataExist(String dbName, String collection,
			String type) {
		boolean flag = false;
		if (MongoDBUtil.collectionExists(dbName, collection)) {
			// TODO:判断MongoDB中是否存在满足条件的数据
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			flag = MongoDBUtil.dataExists(map, dbName, collection);
		} else {
			MongoDBUtil.createCollection(dbName, collection,
					new BasicDBObject());
		}
		return flag;
	}

	public void createMongoTable(String dbName, String collection) {

		if (!MongoDBUtil.collectionExists(dbName, collection)) {
			MongoDBUtil.createCollection(dbName, collection,
					new BasicDBObject());
		}

	}

	/**
	 * 在MongoDB中存储数据
	 * 
	 * @param data
	 *            数据
	 * @param collection
	 *            集合,表
	 * @param type
	 *            数据type
	 */
	public void insertMongoData(List<IndicatorData> data, String collection,
			String type) {
		List<DBObject> _List = new ArrayList<DBObject>();
		for (int i = 0; i < data.size(); i++) {
			_List.add(IndicatorDataToDBOject.convert(data.get(i), type));
		}
		MongoDBUtil.insertBatch(_List, collection);
	}

	public List<DBObject> query(String dbName, String[] collectionNames,
			String type) {
		// 查询数据
		List<DBObject> list = new ArrayList<DBObject>();
		for (String collection : collectionNames) {
			list.addAll(loadMongoData(dbName, collection, type));
		}
		return list;
	}

	/**
	 * 获取所有数据
	 * 
	 * @param type
	 * @param filters
	 * @param collection
	 * 
	 * @return
	 */
	public List<DBObject> loadMongoData(String dbName, String collection,
			String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		return MongoDBUtil.findByRefs(map, dbName, collection);
	}

	public DBObject loadEcDataGroup(String dbName, String collection,
			String type, String isNew, String rfType, String se) {

		Map<String, Object> condMap = new HashMap<String, Object>();
		condMap.put("type", type);
		if (!"-1".equals(isNew)) {
			condMap.put("isNew", isNew);
		}
		if (!"-1".equals(rfType)) {
			condMap.put("rfType", rfType);
		}
		if (!"-1".equals(se)) {
			condMap.put("se", se);
		}
		DBObject key = new BasicDBObject("url", true);
		
		DBObject cond = MongoDBUtil.getMapped(condMap);
		
		DBObject initial = new BasicDBObject("ec", 0);

		String reduce = "function (doc, out) { " + " out.ec += doc.exitCount  }";

		DBObject dBObject = MongoDBUtil.groupByRefs(dbName, collection, key,
				cond, initial, reduce);

		return dBObject;

	}
	
	public DBObject loadEcSummaryDataGroup(String dbName, String collection,
			String type, String isNew, String rfType, String se) {

		Map<String, Object> condMap = new HashMap<String, Object>();
		condMap.put("type", type);
		if (!"-1".equals(isNew)) {
			condMap.put("isNew", isNew);
		}
		if (!"-1".equals(rfType)) {
			condMap.put("rfType", rfType);
		}
		if (!"-1".equals(se)) {
			condMap.put("se", se);
		}
		DBObject key = new BasicDBObject("type", true);
		
		DBObject cond = MongoDBUtil.getMapped(condMap);
		
		DBObject initial = new BasicDBObject("ec", 0);

		String reduce = "function (doc, out) { " + " out.ec += doc.exitCount  }";

		DBObject dBObject = MongoDBUtil.groupByRefs(dbName, collection, key,
				cond, initial, reduce);

		return dBObject;

	}

	public List<DBObject> loadMongoData(String dbName, String collection,
			String type, String isNew) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("isNew", isNew);

		return MongoDBUtil.findByRefs(map, dbName, collection);
	}

}
