package com.cache.ws.mongo;

import com.cache.ws.es.dto.IndicatorData;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import static com.cache.ws.mongo.MongoDBFields.F_KEY_AS_STRING;
import static com.cache.ws.mongo.MongoDBFields.F_PV;
import static com.cache.ws.mongo.MongoDBFields.F_VC;
import static com.cache.ws.mongo.MongoDBFields.F_UV;
import static com.cache.ws.mongo.MongoDBFields.F_NEW_VISITOR_AGGS;
import static com.cache.ws.mongo.MongoDBFields.F_UV_FILTER;
import static com.cache.ws.mongo.MongoDBFields.F_IP;
import static com.cache.ws.mongo.MongoDBFields.F_NUV;
import static com.cache.ws.mongo.MongoDBFields.F_SINGLE_VISITOR_AGGS;
import static com.cache.ws.mongo.MongoDBFields.F_TVT;
import static com.cache.ws.mongo.MongoDBFields.F_TYPE;

/**
 * 用于将ES的对象数据转换为MongoDB的对象数据
 * 
 * @author hydm
 * 
 * @version v001-20150727
 */
public final class IndicatorDataToDBOject {
	private IndicatorDataToDBOject() {
	}

	public static DBObject convert(IndicatorData indic, String type) {
		DBObject dbObject = new BasicDBObject();
		// TODO:ES对象数据变成MongoDB对象数据
		dbObject.put(F_KEY_AS_STRING, indic.getKey_as_string());
		dbObject.put(F_PV, indic.getPv());
		dbObject.put(F_VC, indic.getVc());
		dbObject.put(F_UV, indic.getUv());
		dbObject.put(F_NEW_VISITOR_AGGS, indic.getNew_visitor_aggs());
		dbObject.put(F_UV_FILTER, indic.getUv_filter());
		dbObject.put(F_IP, indic.getIp());
		dbObject.put(F_NUV, indic.getNew_visitor_aggs());
		dbObject.put(F_SINGLE_VISITOR_AGGS, indic.getSingle_visitor_aggs());
		dbObject.put(F_TVT, indic.getTvt());
		dbObject.put(F_TYPE, type);
		return dbObject;
	}

}
