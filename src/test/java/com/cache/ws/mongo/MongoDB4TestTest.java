package com.cache.ws.mongo;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDB4TestTest {
	protected Mongo mg = null;
	protected DB db;
	protected DBCollection users;

	@Before
	public void setUp() throws Exception {
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

	@Test
	public void testCreateCollection() {

		List<DBObject> dbObjects = new ArrayList<DBObject>();

		for (int i = 1; i <= 6; i++) {

			Map<String, String> mapData = new HashMap<String, String>();

			mapData.put("userId", String.valueOf(i));
			mapData.put("pv", String.valueOf(i));
			mapData.put("type", "1");
			mapData.put("isNew", String.valueOf(i%2));

			BasicDBObject dbObject = new BasicDBObject(mapData);

			dbObjects.add(dbObject);
		}

		DBCollection ca = db.createCollection("ga-2015-09-24",
				new BasicDBObject());

		ca.insert(dbObjects);
	}

}
