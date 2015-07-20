package com.cache.ws.mongo;

public class MongoDBSummaryHandle extends MongoDBAbstractHandle {

	@Override
	protected void handleData(String type) {
		System.out.println("处理数据");
	}

}
