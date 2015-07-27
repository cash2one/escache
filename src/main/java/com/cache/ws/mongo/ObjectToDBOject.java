package com.cache.ws.mongo;

import com.cache.ws.es.dto.IndicatorData;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import static com.cache.ws.mongo.MongoDBFields.F_PV;
import static com.cache.ws.mongo.MongoDBFields.F_VC;
import static com.cache.ws.mongo.MongoDBFields.F_UV;
import static com.cache.ws.mongo.MongoDBFields.F_OUT_RATE;
import static com.cache.ws.mongo.MongoDBFields.F_IP;
import static com.cache.ws.mongo.MongoDBFields.F_NUV;
import static com.cache.ws.mongo.MongoDBFields.F_NUV_RATE;
import static com.cache.ws.mongo.MongoDBFields.F_AVG_TIME;
import static com.cache.ws.mongo.MongoDBFields.F_AVG_PAGE;
import static com.cache.ws.mongo.MongoDBFields.F_TYPE;

/**
 * 用于将ES的对象数据转换为MongoDB的对象数据
 * 
 * @author hydm
 * 
 * @version v001-20150727
 */
public final class ObjectToDBOject {
	private ObjectToDBOject() {
	}

	public static DBObject convert(IndicatorData indic, String[] filters,
			String type) {
		DBObject dbObject = new BasicDBObject();
		// TODO:ES对象数据变成MongoDB对象数据
		dbObject.put(F_PV, indic.getPv());
		dbObject.put(F_VC, indic.getVc());
		dbObject.put(F_UV, indic.getUv());
		dbObject.put(F_OUT_RATE, indic.getOutRate());
		dbObject.put(F_IP, indic.getIp());
		dbObject.put(F_NUV, indic.getNuv());
		dbObject.put(F_NUV_RATE, indic.getNuvRate());
		dbObject.put(F_AVG_TIME, indic.getAvgTime());
		dbObject.put(F_AVG_PAGE, indic.getAvgPage());
		// dbObject.put(F_FILTER, Arrays.toString(filters));
		dbObject.put(F_TYPE, type);
		return dbObject;
	}

}
