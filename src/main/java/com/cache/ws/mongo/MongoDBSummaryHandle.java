package com.cache.ws.mongo;

import java.util.List;

import com.mongodb.DBObject;

/**
 * 统计数据处理方法
 * 
 * @author hydm
 *
 */
public class MongoDBSummaryHandle extends MongoDBAbstractHandle {

	@Override
	protected void handleData(List<DBObject> _list, String type) {
		for (DBObject dbObject : _list) {
			System.out.println(dbObject);
		}
	}

}
