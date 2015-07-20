package com.cache.ws.mongo;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author hydm
 * @version 1.0
 */
public class MongoDBUtil {
	private static Mongo connection = null;
	private static DB db = null;
	private static Settings settings;

	static {
		try {
			settings = ImmutableSettings.settingsBuilder()
					.loadFromClasspath("mongo.yml").build();
			connection = new Mongo(settings.get("IP") + ":"
					+ settings.get("PORT"));
			db = connection.getDB(settings.get("DB_NAME"));
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private MongoDBUtil() {

	}

	public static void main(String[] args) {
		MongoDBUtil.find(new BasicDBObject(), "users");
	}

	/**
	 * ���������ж��Ƿ��������
	 */
	public static boolean existByDbs(DBObject dbs, String collName) {
		DBCollection coll = db.getCollection(collName);
		long count = coll.count(dbs);
		return count > 0;
	}

	/**
	 * �жϼ����Ƿ����
	 * 
	 * @param collectionName
	 * @return
	 */
	public static boolean collectionExists(String collectionName) {
		return db.collectionExists(collectionName);
	}

	/**
	 * ��ѯ����,��������ѯ
	 * 
	 * @param id
	 * @param collectionName
	 */
	public static void findById(String id, String collectionName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_id", new ObjectId(id));
		findOne(map, collectionName);
	}

	/**
	 * ��ѯ���� <br>
	 * ------------------------------<br>
	 * 
	 * @param map
	 * @param collectionName
	 */
	public static void findOne(Map<String, Object> map, String collectionName) {
		DBObject dbObject = getMapped(map);
		DBObject object = getCollection(collectionName).findOne(dbObject);
		print(object);
	}

	/**
	 * count
	 * 
	 * @param dbs
	 * @param collectionName
	 * @return
	 */
	public static long count(DBObject dbs, String collectionName) {
		return getCollection(collectionName).count(dbs);
	}

	/**
	 * ��ѯ
	 * 
	 * @param dbObject
	 * @param cursor
	 * @param cursorPreparer
	 * @param collectionName
	 */
	public static void find(DBObject dbObject, String collectionName) {
		DBCursor dbCursor = getCollection(collectionName).find(dbObject);
		Iterator<DBObject> iterator = dbCursor.iterator();
		while (iterator.hasNext()) {
			print(iterator.next());
		}
	}

	/**
	 * ��ȡ����(��)
	 * 
	 * @param collectionName
	 * @return
	 */
	public static DBCollection getCollection(String collectionName) {
		return db.getCollection(collectionName);
	}

	/**
	 * ��ȡ���м���(��)����
	 * 
	 * @return
	 */
	public static Set<String> getCollection() {
		return db.getCollectionNames();
	}

	/**
	 * ��������(��)
	 * 
	 * @param collectionName
	 * @param options
	 */
	public static void createCollection(String collectionName, DBObject options) {
		if (!collectionExists(collectionName)) {
			db.createCollection(collectionName, options);
		}
	}

	/**
	 * ɾ��
	 * 
	 * @param collectionName
	 */
	public static void dropCollection(String collectionName) {
		DBCollection collection = getCollection(collectionName);
		collection.drop();
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static DBObject getMapped(Map<String, Object> map) {
		DBObject dbObject = new BasicDBObject();
		Iterator<Entry<String, Object>> iterable = map.entrySet().iterator();
		while (iterable.hasNext()) {
			Entry<String, Object> entry = iterable.next();
			Object value = entry.getValue();
			String key = entry.getKey();
			if (key.startsWith("$") && value instanceof Map) {
				BasicBSONList basicBSONList = new BasicBSONList();
				Map<String, Object> conditionsMap = ((Map<String, Object>) value);
				Set<String> keys = conditionsMap.keySet();
				for (String k : keys) {
					Object conditionsValue = conditionsMap.get(k);
					if (conditionsValue instanceof Collection) {
						conditionsValue = convertArray(conditionsValue);
					}
					DBObject dbObject2 = new BasicDBObject(k, conditionsValue);
					basicBSONList.add(dbObject2);
				}
				value = basicBSONList;
			} else if (value instanceof Collection) {
				value = convertArray(value);
			} else if (!key.startsWith("$") && value instanceof Map) {
				value = getMapped(((Map<String, Object>) value));
			}
			dbObject.put(key, value);
		}
		return dbObject;
	}

	@SuppressWarnings("unchecked")
	private static Object[] convertArray(Object value) {
		Object[] values = ((Collection<Object>) value).toArray();
		return values;
	}

	private static void print(DBObject object) {
		Set<String> keySet = object.keySet();
		for (String key : keySet) {
			print(object.get(key));
		}
	}

	private static void print(Object object) {
		System.out.println(object.toString());
	}

	/**
	 * ��������
	 * 
	 * @param dbs
	 * @param collName
	 */
	public static void insert(DBObject dbs, String collName) {
		// EG
		// DBObject dbs = new BasicDBObject();
		// dbs.put("name", "uspcat.com");
		// dbs.put("age", 2);
		// List<String> books = new ArrayList<String>();
		// books.add("EXTJS");
		// books.add("MONGODB");
		// dbs.put("books", books);
		// mongoDb.insert(dbs, "users");
		// 1.�õ�����
		// 2.�������
		db.getCollection(collName).insert(dbs);
	}

	/**
	 * ������������
	 * 
	 * @param dbses
	 * @param collName
	 */
	public static void insertBatch(List<DBObject> dbses, String collName) {
		// EG
		// List<DBObject> dbObjects = new ArrayList<DBObject>();
		// DBObject jim = new BasicDBObject("name", "jim");
		// DBObject lisi = new BasicDBObject("name", "lisi");
		// dbObjects.add(jim);
		// dbObjects.add(lisi);
		// mongoDb.insertBatch(dbObjects, "users");
		// 1.�õ�����
		// 2.�������
		db.getCollection(collName).insert(dbses);
	}

	/**
	 * ����idɾ������
	 * 
	 * @param id
	 * @param collName
	 * @return ����Ӱ�����������
	 */
	public static int deleteById(String id, String collName) {
		// EG
		// mongoDb.deleteById("55a778f9f7fe06b241f54b10", "users");
		// 1.�õ�����
		DBObject dbs = new BasicDBObject("_id", new ObjectId(id));
		int count = db.getCollection(collName).remove(dbs).getN();
		return count;
	}

	/**
	 * ��������ɾ������
	 * 
	 * @param id
	 * @param collName
	 * @return ����Ӱ�����������
	 */
	public static int deleteByDbs(DBObject dbs, String collName) {
		// EG
		// DBObject lisi = new BasicDBObject();
		// lisi.put("name", "lisi");
		// int count = mongoDb.deleteByDbs(lisi, "users");
		// System.out.println("ɾ�����ݵ�������: " + count);
		// 1.�õ�����
		DBCollection coll = db.getCollection(collName);
		int count = coll.remove(dbs).getN();
		return count;
	}

	/**
	 * ��������
	 * 
	 * @param find
	 *            ��ѯ��
	 * @param update
	 *            ������
	 * @param upsert
	 *            ���»����
	 * @param multi
	 *            �Ƿ���������
	 * @param collName
	 *            ��������
	 * @return ����Ӱ�����������
	 */
	public static int update(DBObject find, DBObject update, boolean upsert,
			boolean multi, String collName) {
		// EG
		// DBObject find = new BasicDBObject();
		// find.put("name", "С��");
		// DBObject update = new BasicDBObject();
		// update.put("$set", new BasicDBObject("eamil", "test1111@126.com"));
		// mongoDb.update(find, update, false, true, "users");
		// 1.�õ�����
		DBCollection coll = db.getCollection(collName);
		int count = coll.update(find, update, upsert, multi).getN();
		return count;
	}

	/**
	 * ��ѯ��(��ҳ)
	 * 
	 * @param ref
	 * @param keys
	 * @param start
	 * @param limit
	 * @return
	 */
	public DBCursor find(DBObject ref, DBObject keys, int start, int limit,
			String collName) {
		DBCursor cur = find(ref, keys, collName);
		return cur.limit(limit).skip(start);
	}

	/**
	 * ��ѯ��(����ҳ)
	 * 
	 * @param ref
	 * @param keys
	 * @param start
	 * @param limit
	 * @param collName
	 * @return
	 */
	public DBCursor find(DBObject ref, DBObject keys, String collName) {
		// 1.�õ�����
		DBCollection coll = db.getCollection(collName);
		DBCursor cur = coll.find(ref, keys);
		return cur;
	}
}
