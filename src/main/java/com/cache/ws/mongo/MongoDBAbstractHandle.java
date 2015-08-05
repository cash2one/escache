package com.cache.ws.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cache.ws.mongo.dto.ResultData;
import com.mongodb.DBObject;

import static com.cache.ws.mongo.MongoDBFields.F_KEY_AS_STRING;

public abstract class MongoDBAbstractHandle {
	protected List<String> keys = new ArrayList<String>();

	protected List<ResultData> resultDatas = new ArrayList<ResultData>();

	protected Map<String, List<DBObject>> key_list = new HashMap<String, List<DBObject>>();

	/**
	 * 模板方法
	 */
	public void load(List<DBObject> _list, String type) {
		// 调用基本方法
		handleKey(_list);
		handleData();
	}

	/**
	 * 声明处理数据的方法（由子类实现）
	 * 
	 * @param _list
	 * 
	 * @param type
	 */
	protected abstract void handleData();

	/**
	 * 统计所有key值
	 * 
	 * @param _list
	 */
	protected void handleKey(List<DBObject> _list) {
		for (DBObject dbObject : _list) {
			String _key = dbObject.get(F_KEY_AS_STRING).toString();
			if (!keys.contains(_key)) {
				keys.add(_key);
			}
			if (key_list.containsKey(_key)) {
				List<DBObject> _t = key_list.get(_key);
				_t.add(dbObject);
			} else {
				List<DBObject> _t = new ArrayList<DBObject>();
				_t.add(dbObject);
				key_list.put(_key, _t);
			}
		}
	}

}
