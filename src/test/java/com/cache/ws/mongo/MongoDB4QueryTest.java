package com.cache.ws.mongo;

import java.util.List;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;

public class MongoDB4QueryTest extends MongoDB4Test {

	@Test
	public void queryAllField() {
		print("queryAllField");
		DBCollection dbCollection = db.getCollection("users");
		List<DBObject> dbObjects = dbCollection.find().toArray();
		for (DBObject dbObject : dbObjects) {
			print(dbObject);
		}

	}

	// @Test
	public void queryDefField() {
		// 仅仅
		print("queryDefField");
		DBCollection dbCollection = db.getCollection("users");
		DBObject defs = new BasicDBObject();
		defs.put("name", true);
		defs.put("_id", false);
		List<DBObject> dbObjects = dbCollection.find(
				new BasicDBObject("name", new BasicDBObject(
						QueryOperators.EXISTS, true)), defs).toArray();
		for (DBObject dbObject : dbObjects) {
			print(dbObject);
		}

	}
}
