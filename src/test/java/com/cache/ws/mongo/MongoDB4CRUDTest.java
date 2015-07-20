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
		// ��ȡxnmz_weims_test DB�����Ĭ��û�д�����mongodb���Զ�����
		db = mg.getDB("xnmz_weims_test");
		// ��ȡusers DBCollection�����Ĭ��û�д�����mongodb���Զ�����
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
	 * <b>function:</b> ��ѯ��������
	 * 
	 * @author hoojo
	 * @createDate 2011-6-2 ����03:22:40
	 */
	private void queryAll() {
		print("��ѯusers���������ݣ�");
		// db�α�
		DBCursor cur = users.find();
		if (cur.count() == 0) {
			print("��������");
		}
		while (cur.hasNext()) {
			print(cur.next());
		}
	}

	// @Test
	public void add() {
		// �Ȳ�ѯ��������
		queryAll();
		print("count: " + users.count());

		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", 24);
		// users.save(user)���棬getN()��ȡӰ������
		// print(users.save(user).getN());

		// ��չ�ֶΣ���������ֶΣ���Ӱ����������
		user.put("sex", "��");
		print(users.save(user).getN());

		// ��Ӷ������ݣ�����Array����
		print(users.insert(new BasicDBObject("name", "tom"),
				new BasicDBObject("sv", "1111111")).getN());

		// ���List����
		List<DBObject> list = new ArrayList<DBObject>();
		DBObject user1 = new BasicDBObject("name", "happy");
		list.add(user1);
		DBObject user2 = new BasicDBObject("name", "11133333");
		user2.put("age", 22);
		list.add(user2);
		// ���List����
		print(users.insert(list).getN());

		// ��ѯ�����ݣ������Ƿ���ӳɹ�
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
			print("ɾ��id = "
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
		// print("�޸ģ�"
		// + users.update(
		// new BasicDBObject("_id", new ObjectId(
		// "4dde25d06be7c53ffbd70906")),
		// new BasicDBObject("age", 99)).getN());
		// print("�޸ģ�"
		// + users.update(
		// new BasicDBObject("_id", new ObjectId(
		// "4dde2b06feb038463ff09042")),
		// new BasicDBObject("age", 121), true,// ������ݿⲻ���ڣ��Ƿ����
		// false// false:�ж���ֻ�޸ĵ�һ��,true:�ж����Ͳ��޸�
		// ).getN());
		print("�޸ģ�"
				+ users.update(new BasicDBObject("name", "hoojo"),
						new BasicDBObject("name", "dingding"), true,// ������ݿⲻ���ڣ��Ƿ����
						false// false:�ж���ֻ�޸ĵ�һ��,true:�ж����Ͳ��޸�
				).getN());

		// �����ݿⲻ���ھͲ��޸ġ���������ݣ����������ݾͲ��޸�
		// DBObject object = new BasicDBObject("_id", new
		// ObjectId("55ac865a5faa84c0d998e835"));
		// print("�޸Ķ�����"
		// + users.updateMulti(object, new BasicDBObject("name", "199")));
		queryAll();
	}

	// @Test
	public void insert() {
		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", new Random().nextInt(10));
		// users.save(user)���棬getN()��ȡӰ������
		// print(users.save(user).getN());

		// ��չ�ֶΣ���������ֶΣ���Ӱ����������
		user.put("sex", "��");
		print(users.save(user).getN());
	}

	// @Test
	public void query() {
		// ��ѯ����
		// queryAll();

		// ��ѯid = 55ac889a5faa4a4e7fd376fc
		// print("find by id = 55ac889a5faa4a4e7fd376fc : "
		// + users.find(new BasicDBObject("_id", new ObjectId(
		// "55ac889a5faa4a4e7fd376fc"))).toArray());

		// ��ѯage = 24
		// print("find age = 24 : " + users.find(new BasicDBObject("age",
		// 24)).toArray());

		// ��ѯage >= 24
		// print("find age >= 24 : " + users.find(new BasicDBObject("age", new
		// BasicDBObject("$gte", 24))).toArray());

		// ��ѯage <= 24
		// print("find age <= 24 : " + users.find(new BasicDBObject("age", new
		// BasicDBObject("$lte", 24))).toArray());

		// ��ѯage != 25
		// print("find age != 25 : " + users.find(new BasicDBObject("age", new
		// BasicDBObject("$ne", 25))).toArray());

		// "��ѯage in 25/26/27
		// print("find age in 25/26/27 : "
		// + users.find(
		// new BasicDBObject("age", new BasicDBObject(
		// QueryOperators.IN, new int[] { 6, 26, 27 })))
		// .toArray());

		// ��ѯage not in 25/26/27
		// print("find age not in 25/26/27 : "
		// + users.find(
		// new BasicDBObject("age", new BasicDBObject(
		// QueryOperators.NIN, new int[] { 25, 26, 27 })))
		// .toArray());

		// ��ѯage exists
		// print("find age exists : "
		// + users.find(
		// new BasicDBObject("age", new BasicDBObject(
		// QueryOperators.EXISTS, true))).toArray());

		// print("ֻ��ѯage���ԣ�"
		// + users.find(null, new BasicDBObject("age", true)).toArray());

		// ֻ��ѯһ�����ݣ�����ȥ��һ��
		// print("findOne: " + users.findOne());
		// print("findOne: " + users.findOne(new BasicDBObject("age", 24)));
		// print("findOne: "
		// + users.findOne(new BasicDBObject("age", 24),
		// new BasicDBObject("name", true)));

		// ��ѯ�޸ġ�ɾ��,��һ��
		// print("findAndRemove ��ѯage=25�����ݣ�����ɾ��: "
		// + users.findAndRemove(new BasicDBObject("name", "hoojo")));

		// ��ѯage=26�����ݣ������޸�name��ֵΪAbc
		// print("findAndModify: "
		// + users.findAndModify(new BasicDBObject("name", "hoojo"),
		// new BasicDBObject("age", 111)));

		print("findAndModify: "
				+ users.findAndModify(new BasicDBObject("age", 28), // ��ѯage=28������
						new BasicDBObject("name", true), // ��ѯname����
						new BasicDBObject("age", true), // ����age����
						false, // �Ƿ�ɾ����true��ʾɾ��
						new BasicDBObject("name", "Abc"), // �޸ĵ�ֵ����name�޸ĳ�Abc
						true, true));

		queryAll();
	}

	@Test
	public void dataExists() {
		queryAll();
		DBObject dbObject = new BasicDBObject();
		dbObject.put("name", "11199");
		print("name = 199 �ļ�¼�Ƿ���� : " + users.findOne(dbObject));
	}
}
