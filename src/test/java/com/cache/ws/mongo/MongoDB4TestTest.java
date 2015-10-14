package com.cache.ws.mongo;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.cache.ws.util.GaDateUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDB4TestTest {
	protected Mongo mg = null;
	protected DB db;
	protected DB adminDB;
	protected DBCollection users;

	@Before
	public void setUp() throws Exception {
		try {
			mg = new Mongo("192.168.100.5", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		// 获取xnmz_weims_test DB；如果默认没有创建，mongodb会自动创建
		db = mg.getDB("dev");
		adminDB =  mg.getDB("admin");

	}

	@Test
	public void testCreateCollection() throws InstantiationException, IllegalAccessException {

		List<DBObject> dbObjects = new ArrayList<DBObject>();

		
		List<String> dates = GaDateUtils.getDaysListBetweenDates(30, "2015-10-06");
		
		
		for(String date : dates) {
		for (int i = 1; i <= new Random().nextInt(150); i++) {

			Map<String, String> mapData = new HashMap<String, String>();

			mapData.put("userId", String.valueOf(i));
			mapData.put("pv", String.valueOf(i));
			mapData.put("type", "1");
			mapData.put("isNew", String.valueOf(i%2));

			BasicDBObject dbObject = new BasicDBObject(mapData);

			dbObjects.add(dbObject);
		}
		
		
		//创建数据库
		DBCollection ca = db.createCollection("ga-"+date,
				new BasicDBObject());

	
		CommandResult cr = null;
		
		//支持分片
		BasicDBObject comfirmShard = new BasicDBObject();
		Map<Object,Object> shardMap = new HashMap<Object,Object>();
		shardMap.put("shardcollection", "dev.ga-"+date);
		shardMap.put("key", new BasicDBObject("userId",1));
		shardMap.put("enablesharding", "dev");
		
		
		comfirmShard.putAll(shardMap);
		cr = adminDB.command(comfirmShard);
		System.out.println(cr.toString());
	    
	    
		ca.insert(dbObjects);
		

		System.out.println(db.getCollection("ga-"+date).getStats());
		System.out.println(ca.getIndexInfo());
		
		}
		
	
	}

}
