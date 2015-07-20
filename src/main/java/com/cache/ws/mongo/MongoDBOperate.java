package com.cache.ws.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class MongoDBOperate {
	public List<DBObject> query(String[] collectionNames, String[] filters,
			String type) {
		// �ж������Ƿ���ڲ��Ҵ洢����
		for (String collection : collectionNames) {
			query(collection, filters, type);
		}
		// ��ѯ����
		List<DBObject> list = new ArrayList<DBObject>();
		for (String collection : collectionNames) {
			list.addAll(loadMongoData(collection, filters, type));
		}
		return list;
	}

	private void query(String collection, String[] filters, String type) {
		if (isMongoDataExist(collection, filters, type)) {
			// ES�л�ȡ����
			// MongoDB�洢
			List<?> data = new ArrayList<Object>();
			insertMongoData(data, collection, filters, type);
		}
	}

	/**
	 * ��MongoDB���ж������Ƿ����
	 * 
	 * @param type
	 *            ����type
	 * @param filters
	 *            ��������
	 * @param collection
	 *            ����,��
	 * 
	 * @return
	 */
	private boolean isMongoDataExist(String collection, String[] filters,
			String type) {
		boolean flag = false;
		if (MongoDBUtil.collectionExists(collection)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			map.put("filter", Arrays.toString(filters));
			flag = MongoDBUtil.dataExists(map, collection);
		} else {
			MongoDBUtil.createCollection(collection, new BasicDBObject());
		}
		return flag;
	}

	/**
	 * ��MongoDB�д洢����
	 * 
	 * @param data
	 *            ����
	 * @param collection
	 *            ����,��
	 * @param filters
	 *            ��������
	 * @param type
	 *            ����type
	 */
	private void insertMongoData(List<?> data, String collection,
			String[] filters, String type) {
		List<DBObject> _List = new ArrayList<DBObject>();
		for (int i = 0; i < data.size(); i++) {
			_List.add(ObjectToDBOject.convert(data.get(i), filters, type));
		}
		MongoDBUtil.insertBatch(_List, collection);
	}

	/**
	 * ��ȡ��������
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
		map.put("filter", Arrays.toString(filters));
		return MongoDBUtil.findByRefs(map, collection);
	}
}
