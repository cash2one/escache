package com.cache.ws.mongo;

import java.util.List;

import com.mongodb.DBObject;

public abstract class MongoDBAbstractHandle {
	/**
	 * ģ�巽��
	 */
	public void load(String[] collectionNames, String[] filters, String type) {
		// ���û�������
		loadData(collectionNames, filters, type);
		hookMethod();
		handleData(type);
	}

	/**
	 * ����������������������ʵ�֣�
	 * 
	 * @param type
	 */
	protected abstract void handleData(String type);

	/**
	 * ��������(�շ���)
	 */
	protected void hookMethod() {
		System.out.println("���ݻ�ȡ�ɹ�....");
	}

	/**
	 * �����������Ѿ�ʵ�֣�
	 */
	private final List<DBObject> loadData(String[] collectionNames,
			String[] filters, String type) {
		// ҵ����صĴ���
		MongoDBOperate operate = new MongoDBOperate();
		return operate.query(collectionNames, filters, type);
	}
}
