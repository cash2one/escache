package com.cache.ws.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.cache.ws.constant.GaConstant;
import com.cache.ws.util.GaDateUtils;
import com.cache.ws.util.GaUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

public class MongoDB4TestTest {
	protected Mongo mg = null;
	protected DB db;
	protected DB exitDB;

	protected DB adminDB;
	protected DBCollection users;

	//@Before
	public void setUp() throws Exception {
		try {
			mg = new Mongo("192.168.100.10", 23135);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		// 获取xnmz_weims_test DB；如果默认没有创建，mongodb会自动创建
		db = mg.getDB("dev");
		exitDB = mg.getDB("exit");
		adminDB = mg.getDB("admin");

	}

	 //@Test
	public void addData() {

		DBCollection collExit = exitDB.createCollection(
				"exit-indicator-2015-11-25", null);
		
		

//		 List<String> strs = new ArrayList<String>();
//		
//		 DBObject queryObject = new BasicDBObject();
//		 queryObject.put("_id", "A");
//		 queryObject.put("score", 100);
//		 // queryObject.put("pendingTransactions",strs);
//		
//		 collExit.save(queryObject);
//		
//		 DBObject queryObject1 = new BasicDBObject();
//		 queryObject1.put("_id", "B");
//		 queryObject1.put("score", 100);
//		 // queryObject1.put("pendingTransactions",strs);
//		
//		 collExit.save(queryObject1);
	}

	public void transatcion() {

		DBCollection coll = exitDB.getCollection("transatcion");
		DBCollection collExit = exitDB.getCollection("exit-indicator-2015-11-25");
		DBObject initialObject = new BasicDBObject();
		initialObject.put("from", "A");
		initialObject.put("to", "B");
		initialObject.put("score", 10);
		initialObject.put("state", "initial");
		initialObject.put("_id", "11");

		WriteResult wr = coll.insert(initialObject);

		DBObject pendingObject = new BasicDBObject();
		pendingObject.put("state", "initial");
		pendingObject = coll.findOne(pendingObject);

		DBObject qObject = new BasicDBObject();
		qObject.put("_id", pendingObject.get("_id"));
		qObject.put("state", "initial");

		DBObject uObject = new BasicDBObject();
		uObject.put("$set", new BasicDBObject("state", "pending"));

		coll.update(qObject, uObject);

		DBObject queryObject = new BasicDBObject();
		queryObject.put("_id", pendingObject.get("from"));
		queryObject.put("pendingTransactions", new BasicDBObject("$ne",
				pendingObject.get("_id")));

		DBObject updateObject = new BasicDBObject();
		updateObject.put("$inc", new BasicDBObject("score", -10));
		updateObject.put("$push", new BasicDBObject("pendingTransactions",
				pendingObject.get("_id")));

		collExit.update(queryObject, updateObject, true, false);

		DBObject queryObject1 = new BasicDBObject();
		queryObject1.put("_id", pendingObject.get("to"));
		queryObject1.put("pendingTransactions", new BasicDBObject("$ne",
				pendingObject.get("_id")));

		DBObject updateObject1 = new BasicDBObject();
		updateObject1.put("$inc", new BasicDBObject("score", 10));
		updateObject1.put("$push", new BasicDBObject("pendingTransactions",
				pendingObject.get("_id")));

		collExit.update(queryObject1, updateObject1, true, false);

		DBObject initialObject1 = new BasicDBObject();
		initialObject1.put("state", "pending");
		initialObject1.put("_id", pendingObject.get("_id"));

		DBObject pendingObject1 = new BasicDBObject();
		pendingObject1.put("$set", new BasicDBObject("state", "applied"));

		coll.update(initialObject1, pendingObject1);

		DBObject queryObject2 = new BasicDBObject();
		queryObject2.put("_id", pendingObject.get("from"));
		queryObject2.put("pendingTransactions", pendingObject.get("_id"));

		DBObject updateObject2 = new BasicDBObject();
		updateObject2.put("$pull", new BasicDBObject("pendingTransactions",
				pendingObject.get("_id")));

		collExit.update(queryObject2, updateObject2);

		DBObject queryObject3 = new BasicDBObject();
		queryObject3.put("_id", pendingObject.get("to"));
		queryObject3.put("pendingTransactions", pendingObject.get("_id"));

		DBObject updateObject3 = new BasicDBObject();
		updateObject3.put("$pull", new BasicDBObject("pendingTransactions",
				pendingObject.get("_id")));

		collExit.update(queryObject3, updateObject3);

	}

	public void testCreateCollection() {

		List<DBObject> dbObjects = new ArrayList<DBObject>();

		List<String> dates = GaDateUtils.getDaysListBetweenDates(1,
				"2015-10-19");

		dates = GaUtils.getMongDBName(GaConstant.DAY, dates);

		for (String date : dates) {
			System.out.println(date);
			for (int i = 1; i <= new Random().nextInt(15000); i++) {

				Map<String, String> mapData = new HashMap<String, String>();

				mapData.put("userId", String.valueOf(i));
				mapData.put("pv", String.valueOf(i));
				mapData.put("type", "1");
				mapData.put("isNew", String.valueOf(i % 2));

				BasicDBObject dbObject = new BasicDBObject(mapData);

				dbObjects.add(dbObject);
			}

			// 创建数据库
			DBCollection ca = db.createCollection(date, new BasicDBObject());

			CommandResult cr = null;

			// 支持分片
			BasicDBObject comfirmShard = new BasicDBObject();
			Map<Object, Object> shardMap = new HashMap<Object, Object>();
			shardMap.put("enablesharding", "dev");
			shardMap.put("shardcollection", "dev." + date);

			shardMap.put("key", new BasicDBObject("userId", 1));

			comfirmShard.putAll(shardMap);
			cr = adminDB.command(comfirmShard);

			System.out.println(cr.toString());

			BasicDBObject comfirmShard1 = new BasicDBObject();

			cr = db.command(comfirmShard1);
			System.out.println(cr.toString());

			ca.insert(dbObjects);

			System.out.println(db.getCollection(date).getStats());
			System.out.println(ca.getIndexInfo());

		}

	}

	//@Test
	public void saveDate() {

		Map<String, Object> source = new HashMap<String, Object>();
		source.put("rf", "http://loc1");
		source.put("loc", "http://loc2");
		source.put("rf_type", "2");
		source.put("se", "Google");
		source.put("isNew", "0");
		source.put("tt", "dingwei");
		source.put("type","564d8b584c59da027cba765e818cc246");

		// 退出次数对象
		ExitCountObject exitCount = createExitCount(source);
		if (exitCount == null) {
			return;
		}

		// 连接表名称
		DBCollection collTransatcion = exitDB.getCollection("transatcion");
		DBCollection collExit = exitDB.getCollection("exit-indicator-2015-11-25");

		if (!"-".equals(exitCount.getRf())) { // 来源路径为 "-".
			DBObject transatcionObject = getTransatcionObject(
					exitCount.getLoc(), exitCount.getRf());

			// 开启事物
			WriteResult transatcionWR = collTransatcion
					.insert(transatcionObject);

			if (transatcionWR.getError() == null) {

				// 更新事物状态
				transatcionWR = collTransatcion
						.update(getUpdateTransatcionObject(transatcionObject
								.get("_id")),
								getUpdateTransatcionStatus("pending"));

				if (transatcionWR.getError() == null) {

					// 第一次提交
					boolean isSuccess = firstSumbit(transatcionObject,
							exitCount, collExit);

					if (isSuccess) {

						// 更新事物状态
						transatcionWR = collTransatcion.update(
								getUpdateTransatcionObject(transatcionObject
										.get("_id")),
								getUpdateTransatcionStatus("applied"));

						if (transatcionWR.getError() == null) {

							// 第二次提交
							isSuccess = sencondSubmit(transatcionObject,
									exitCount, collExit);

							if (isSuccess) {

								// 完成
								transatcionWR = collTransatcion
										.update(getUpdateTransatcionObject(transatcionObject
												.get("_id")),
												getUpdateTransatcionStatus("done"));

							}
						}
					}
				}

			}

		}

	}

	private boolean sencondSubmit(DBObject transatcionObject,
			ExitCountObject exitCount, DBCollection collExit) {
		// 第二次提交
		DBObject rfPullQuery = getPullExitCountObject(transatcionObject,
				exitCount, true);

		DBObject rfPullUpdate = new BasicDBObject();
		rfPullUpdate.put("$pull", new BasicDBObject("pendingTransactions",
				transatcionObject.get("_id")));

		WriteResult rfPullWR = collExit.update(rfPullQuery, rfPullUpdate);
		String rfPullWRError = rfPullWR.getError();

		DBObject locPullQuery = getPullExitCountObject(transatcionObject,
				exitCount, false);

		DBObject locPullUpdate = new BasicDBObject();
		locPullUpdate.put("$pull", new BasicDBObject("pendingTransactions",
				transatcionObject.get("_id")));

		WriteResult locPullWR = collExit.update(locPullQuery, locPullUpdate);

		if (rfPullWRError == null && locPullWR.getError() == null) {
			return true;
		}
		return false;

	}

	private boolean firstSumbit(DBObject transatcionObject,
			ExitCountObject exitCount, DBCollection collExit) {

	
		DBObject rfQuery = getExitCountObject(transatcionObject, exitCount,
				true);
	
		DBObject rfUpdate = new BasicDBObject();
		rfUpdate.put("$push", new BasicDBObject("pendingTransactions",
				transatcionObject.get("_id")));
		
		DBObject dBObject = collExit.findOne(rfQuery);
		//是否存在
		if(dBObject == null) {
			rfUpdate.put("$inc", new BasicDBObject("exitCount", 0));
		} else {
			Integer eCount = Integer.valueOf(dBObject.get("exitCount").toString());
			if(eCount > 0) {
				rfUpdate.put("$inc", new BasicDBObject("exitCount", -1));
			} else {
				rfUpdate.put("$inc", new BasicDBObject("exitCount", 0));
			}
		}
		
	
		WriteResult rfWR = collExit.update(rfQuery, rfUpdate, true, false);


		String rfWRError = rfWR.getError();

		DBObject locQuery = getExitCountObject(transatcionObject, exitCount,
				false);
		DBObject locUpdate = new BasicDBObject();
		locUpdate.put("$inc", new BasicDBObject("exitCount", 1));
		locUpdate.put("$push", new BasicDBObject("pendingTransactions",
				transatcionObject.get("_id")));

		WriteResult locWR = collExit.update(locQuery, locUpdate, true, false);

		if (rfWRError == null && locWR.getError() == null) {
			return true;
		}
		return false;

	}

	private DBObject getPullExitCountObject(DBObject transatcionObject,
			ExitCountObject exitCount, boolean isRf) {

		DBObject queryObject = new BasicDBObject();

		queryObject.put("tt", exitCount.getTt());
		if (isRf) {
			queryObject.put("url", exitCount.getRf());
		} else {
			queryObject.put("url", exitCount.getLoc());
		}
		queryObject.put("rfType", exitCount.getRfType());
		queryObject.put("isNew", exitCount.getIsNew());
		if (StringUtils.isNotBlank(exitCount.getSe())) {
			queryObject.put("se", exitCount.getSe());
		}

		return queryObject;

	}

	private DBObject getExitCountObject(DBObject transatcionObject,
			ExitCountObject exitCount, boolean isRf) {

		DBObject queryObject = new BasicDBObject();

		queryObject.put("type", exitCount.getType());
		queryObject.put("tt", exitCount.getTt());
		if (isRf) {
			queryObject.put("url", exitCount.getRf());
		} else {
			queryObject.put("url", exitCount.getLoc());
		}

		queryObject.put("rfType", exitCount.getRfType());
		queryObject.put("isNew", exitCount.getIsNew());
		if (StringUtils.isNotBlank(exitCount.getSe())) {
			queryObject.put("se", exitCount.getSe());
		}
		queryObject.put("pendingTransactions", new BasicDBObject("$ne",
				transatcionObject.get("_id")));

		return queryObject;

	}

	private DBObject getUpdateTransatcionObject(Object id) {
		DBObject dBObjectId = new BasicDBObject();
		dBObjectId.put("_id", id);
		return dBObjectId;
	}

	private DBObject getUpdateTransatcionStatus(Object status) {
		DBObject dBObject = new BasicDBObject();
		DBObject parameterObject = new BasicDBObject("state", status);
		parameterObject.put("lastModified", GaDateUtils.getCurrentTime());
		dBObject.put("$set", parameterObject);
		return dBObject;
	}

	private DBObject getTransatcionObject(String loc, String rf) {
		DBObject transatcionObject = new BasicDBObject();
		transatcionObject.put("from", rf);
		transatcionObject.put("to", loc);
		transatcionObject.put("score", 1);
		transatcionObject.put("state", "initial");
		transatcionObject.put("lastModified", GaDateUtils.getCurrentTime());

		return transatcionObject;
	}

	private ExitCountObject createExitCount(Map<String, Object> source) {

		// 用户ID
		String tt = source.containsKey("tt") ? source.get("tt").toString() : "";

		String type = source.get("type").toString();
		
		if (StringUtils.isBlank(tt)) {
			return null;
		}
		// 当前路径
		String loc = source.containsKey("loc") ? source.get("loc").toString()
				: "";
		if (StringUtils.isBlank(loc)) {
			return null;
		}
		// 来源路径
		String rf = source.containsKey("rf") ? source.get("rf").toString() : "";
		// 来源类型
		String rfType = source.containsKey("rf_type") ? source.get("rf_type")
				.toString() : "";
		// 搜素引擎名字
		String se = source.containsKey("se") ? source.get("se").toString() : "";
		// 新老访客
		String isNew = source.containsKey("isNew") ? source.get("isNew")
				.toString() : "";
				
		

		return new ExitCountObject(type,tt,loc, rf, rfType, se, isNew);
	}

	public DBObject getLocQueryObject(ExitCountObject exitCount) {
		DBObject queryObject = new BasicDBObject();
		queryObject.put("url", exitCount.getLoc());

		if (StringUtils.isNotBlank(exitCount.getRfType())) {
			queryObject.put("rfType", exitCount.getRfType());
		}
		if (StringUtils.isNotBlank(exitCount.getSe())) {
			queryObject.put("se", exitCount.getSe());
		}
		if (StringUtils.isNotBlank(exitCount.getIsNew())) {
			queryObject.put("isNew", exitCount.getIsNew());
		}
		return queryObject;
	}

	public DBObject getLocUpdateObject(int value) {
		DBObject updateObject = new BasicDBObject();
		updateObject.put("$inc", new BasicDBObject("exitCount", value));
		updateObject
				.put("$set",
						new BasicDBObject("lastModified", GaDateUtils
								.getCurrentTime()));

		return updateObject;
	}

}
