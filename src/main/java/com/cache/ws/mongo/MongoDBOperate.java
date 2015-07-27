package com.cache.ws.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cache.ws.es.dto.IndicatorData;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class MongoDBOperate {

	/**
	 * 在MongoDB中判断数据是否存在
	 * 
	 * @param type
	 *            数据type
	 * @param filters
	 *            过滤条件
	 * @param collection
	 *            集合,表
	 * 
	 * @return
	 */
	public boolean isMongoDataExist(String collection, String[] filters,
			String type) {
		boolean flag = false;
		if (MongoDBUtil.collectionExists(collection)) {
			// TODO:判断MongoDB中是否存在满足条件的数据
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			// map.put("filter", Arrays.toString(filters));
			flag = MongoDBUtil.dataExists(map, collection);
		} else {
			MongoDBUtil.createCollection(collection, new BasicDBObject());
		}
		return flag;
	}

	/**
	 * 在MongoDB中存储数据
	 * 
	 * @param data
	 *            数据
	 * @param collection
	 *            集合,表
	 * @param filters
	 *            过滤条件
	 * @param type
	 *            数据type
	 */
	public void insertMongoData(List<IndicatorData> data, String collection,
			String[] filters, String type) {
		List<DBObject> _List = new ArrayList<DBObject>();
		for (int i = 0; i < data.size(); i++) {
			_List.add(ObjectToDBOject.convert(data.get(i), filters, type));
		}
		MongoDBUtil.insertBatch(_List, collection);
	}

	public List<DBObject> query(String[] collectionNames, String[] filters,
			String type) {
		// 查询数据
		List<DBObject> list = new ArrayList<DBObject>();
		for (String collection : collectionNames) {
			list.addAll(loadMongoData(collection, filters, type));
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
	private List<DBObject> loadMongoData(String collection, String[] filters,
			String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		// map.put("filter", Arrays.toString(filters));
		return MongoDBUtil.findByRefs(map, collection);
	}

}
