package com.cache.ws.exit.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cache.ws.constant.ExitConstant;
import com.cache.ws.mongo.MongoDBOperate;
import com.mongodb.DBObject;

@Component
public class ExitCountDao {

	@Autowired
	private MongoDBOperate mongoDBOperate;

	public DBObject queryExitCount(String table, String type, String isNew,
			String rfType, String se) {

		DBObject data = null;

		data = mongoDBOperate.loadMongoDataGroup(ExitConstant.DB_NAME, table,
				type, isNew, rfType, se);

		return data;
	}

}
