package com.cache.ws.mongo;

import java.util.List;

import com.mongodb.DBObject;

public abstract class MongoDBAbstractHandle {
	/**
	 * 模板方法
	 */
	public void load(String[] collectionNames, String[] filters, String type) {
		// 调用基本方法
		List<DBObject> _list = loadData(collectionNames, filters, type);
		hookMethod();
		handleData(_list, type);
	}

	/**
	 * 基本方法的声明（由子类实现）
	 * @param _list 
	 * 
	 * @param type
	 */
	protected abstract void handleData(List<DBObject> _list, String type);

	/**
	 * 基本方法(空方法)
	 */
	protected void hookMethod() {
		System.out.println("数据获取成功....");
	}

	/**
	 * 基本方法（已经实现）
	 */
	private final List<DBObject> loadData(String[] collectionNames,
			String[] filters, String type) {
		// 业务相关的代码
		MongoDBOperate operate = new MongoDBOperate();
		return operate.query(collectionNames, filters, type);
	}
}
