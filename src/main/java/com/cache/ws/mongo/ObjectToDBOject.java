package com.cache.ws.mongo;

import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class ObjectToDBOject {
	private ObjectToDBOject() {
	}

	public static DBObject convert(Object object, String[] filters, String type) {
		DBObject dbObject = new BasicDBObject();

		// TODO:ES对象数据变成MongoDB对象数据
		dbObject.put("pv", "9089");
		dbObject.put("uv", "4444");
		dbObject.put("ip", "1232");
		dbObject.put("filter", Arrays.toString(filters));
		dbObject.put("type", type);
		return dbObject;
	}
}
