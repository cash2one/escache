package com.cache.ws.mongo;

import java.util.List;

import com.mongodb.DBObject;

public class MongoDBSummaryHandle extends MongoDBAbstractHandle {

	@Override
	protected void handleData(List<DBObject> _list, String type) {
		System.out.println("处理数据");
		for (DBObject dbObject : _list) {
			System.out.println(dbObject);
		}
	}

}
