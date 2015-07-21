package com.cache.ws.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDB4CRUDTest {
	private Mongo mg = null;
	private DB db;
	private DBCollection users;

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
		// 获取users DBCollection；如果默认没有创建，mongodb会自动创建
		users = db.getCollection("users");
	}

	@After
	public void destory() {
		if (mg != null) {
			mg.close();
		}
		mg = null;
		db = null;
		users = null;
		System.gc();
	}

	public void print(Object o) {
		System.out.println(o);
	}

	/**
	 * <b>function:</b> 查询所有数据
	 * 
	 * @author hoojo
	 * @createDate 2011-6-2 下午03:22:40
	 */
	private void queryAll() {
		print("查询users的所有数据：");
		// db游标
		DBCursor cur = users.find();
		if (cur.count() == 0) {
			print("暂无数据");
		}
		while (cur.hasNext()) {
			print(cur.next());
		}
	}

	// @Test
	public void add() {
		// 先查询所有数据
		queryAll();
		print("count: " + users.count());

		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", 24);
		// users.save(user)保存，getN()获取影响行数
		// print(users.save(user).getN());

		// 扩展字段，随意添加字段，不影响现有数据
		user.put("sex", "男");
		print(users.save(user).getN());

		// 添加多条数据，传递Array对象
		print(users.insert(new BasicDBObject("name", "tom"),
				new BasicDBObject("sv", "1111111")).getN());

		// 添加List集合
		List<DBObject> list = new ArrayList<DBObject>();
		DBObject user1 = new BasicDBObject("name", "happy");
		list.add(user1);
		DBObject user2 = new BasicDBObject("name", "11133333");
		user2.put("age", 22);
		list.add(user2);
		// 添加List集合
		print(users.insert(list).getN());

		// 查询下数据，看看是否添加成功
		print("count: " + users.count());
		queryAll();
	}

	// @Test
	public void remove() {
		queryAll();
		DBCursor cur = users.find();
		while (cur.hasNext()) {
			DBObject o = cur.next();
			print(o.get("_id"));
			print("删除id = "
					+ o.get("_id")
					+ " : "
					+ users.remove(
							new BasicDBObject("_id", new ObjectId(o.get("_id")
									.toString()))).getN());
		}
		queryAll();
	}

	// @Test
	public void modify() {
		queryAll();
		// print("修改："
		// + users.update(
		// new BasicDBObject("_id", new ObjectId(
		// "4dde25d06be7c53ffbd70906")),
		// new BasicDBObject("age", 99)).getN());
		// print("修改："
		// + users.update(
		// new BasicDBObject("_id", new ObjectId(
		// "4dde2b06feb038463ff09042")),
		// new BasicDBObject("age", 121), true,// 如果数据库不存在，是否添加
		// false// false:有多条只修改第一条,true:有多条就不修改
		// ).getN());
		print("修改："
				+ users.update(new BasicDBObject("name", "hoojo"),
						new BasicDBObject("name", "dingding"), true,// 如果数据库不存在，是否添加
						false// false:有多条只修改第一条,true:有多条就不修改
				).getN());

		// 当数据库不存在就不修改、不添加数据，当多条数据就不修改
		// DBObject object = new BasicDBObject("_id", new
		// ObjectId("55ac865a5faa84c0d998e835"));
		// print("修改多条："
		// + users.updateMulti(object, new BasicDBObject("name", "199")));
		queryAll();
	}

	// @Test
	public void insert() {
		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", new Random().nextInt(10));
		// users.save(user)保存，getN()获取影响行数
		// print(users.save(user).getN());

		// 扩展字段，随意添加字段，不影响现有数据
		user.put("sex", "男");
		print(users.save(user).getN());
	}

	// @Test
	public void query() {
		// 查询所有
		// queryAll();

		// 查询id = 55ac889a5faa4a4e7fd376fc
		// print("find by id = 55ac889a5faa4a4e7fd376fc : "
		// + users.find(new BasicDBObject("_id", new ObjectId(
		// "55ac889a5faa4a4e7fd376fc"))).toArray());

		// 查询age = 24
		// print("find age = 24 : " + users.find(new BasicDBObject("age",
		// 24)).toArray());

		// 查询age >= 24
		// print("find age >= 24 : " + users.find(new BasicDBObject("age", new
		// BasicDBObject("$gte", 24))).toArray());

		// 查询age <= 24
		// print("find age <= 24 : " + users.find(new BasicDBObject("age", new
		// BasicDBObject("$lte", 24))).toArray());

		// 查询age != 25
		// print("find age != 25 : " + users.find(new BasicDBObject("age", new
		// BasicDBObject("$ne", 25))).toArray());

		// "查询age in 25/26/27
		// print("find age in 25/26/27 : "
		// + users.find(
		// new BasicDBObject("age", new BasicDBObject(
		// QueryOperators.IN, new int[] { 6, 26, 27 })))
		// .toArray());

		// 查询age not in 25/26/27
		// print("find age not in 25/26/27 : "
		// + users.find(
		// new BasicDBObject("age", new BasicDBObject(
		// QueryOperators.NIN, new int[] { 25, 26, 27 })))
		// .toArray());

		// 查询age exists
		// print("find age exists : "
		// + users.find(
		// new BasicDBObject("age", new BasicDBObject(
		// QueryOperators.EXISTS, true))).toArray());

		// print("只查询age属性："
		// + users.find(null, new BasicDBObject("age", true)).toArray());

		// 只查询一条数据，多条去第一条
		// print("findOne: " + users.findOne());
		// print("findOne: " + users.findOne(new BasicDBObject("age", 24)));
		// print("findOne: "
		// + users.findOne(new BasicDBObject("age", 24),
		// new BasicDBObject("name", true)));

		// 查询修改、删除,第一条
		// print("findAndRemove 查询age=25的数据，并且删除: "
		// + users.findAndRemove(new BasicDBObject("name", "hoojo")));

		// 查询age=26的数据，并且修改name的值为Abc
		// print("findAndModify: "
		// + users.findAndModify(new BasicDBObject("name", "hoojo"),
		// new BasicDBObject("age", 111)));

		print("findAndModify: "
				+ users.findAndModify(new BasicDBObject("age", 28), // 查询age=28的数据
						new BasicDBObject("name", true), // 查询name属性
						new BasicDBObject("age", true), // 按照age排序
						false, // 是否删除，true表示删除
						new BasicDBObject("name", "Abc"), // 修改的值，将name修改成Abc
						true, true));

		queryAll();
	}

	@Test
	public void dataExists() {
		queryAll();
		DBObject dbObject = new BasicDBObject();
		dbObject.put("name", "11199");
		print("name = 199 的记录是否存在 : " + users.findOne(dbObject));
	}
}
