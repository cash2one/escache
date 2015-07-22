package com.cache.ws.mongo;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.QueryOperators;

public class MongoDB4QueryTest {
	private Mongo mg = null;
	private DB db;

	@Before
	public void init() {
		try {
			mg = new Mongo("192.168.100.10", 23135);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		// 获取xnmz_weims_test DB；如果默认没有创建，mongodb会自动创建
		db = mg.getDB("xnmz_weims_test");
	}

	@After
	public void destory() {
		if (mg != null) {
			mg.close();
		}
		mg = null;
		db = null;
		System.gc();
	}

	public void print(Object o) {
		System.out.println(o);
	}

	@Test
	public void queryAllField() {
		print("queryAllField");
		DBCollection dbCollection = db.getCollection("users");
		List<DBObject> dbObjects = dbCollection.find().toArray();
		for (DBObject dbObject : dbObjects) {
			print(dbObject);
		}

	}

//	@Test
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
