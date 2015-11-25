package com.cache.ws.ga.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cache.ws.constant.GaConstant;
import com.cache.ws.mongo.MongoDBOperate;
import com.mongodb.DBObject;

@Component
public class GroupAnalyticsDao {

	@Autowired
	private MongoDBOperate mongoDBOperate;

	public List<DBObject> queryGaData(String table, String type) {
		List<DBObject> datas = null;
		datas = mongoDBOperate.loadMongoData(GaConstant.DB_NAME,table, type);

		return datas;
	}
	
	
	public List<DBObject> queryGaData(String table, String type,String isNew) {
		List<DBObject> datas = null;
		datas = mongoDBOperate.loadMongoData(GaConstant.DB_NAME,table, type,isNew);

		return datas;
	}

}
