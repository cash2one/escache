package com.cache.ws.mongo;

import static com.cache.ws.mongo.MongoDBFields.F_IP;
import static com.cache.ws.mongo.MongoDBFields.F_NEW_VISITOR_AGGS;
import static com.cache.ws.mongo.MongoDBFields.F_NUV;
import static com.cache.ws.mongo.MongoDBFields.F_PV;
import static com.cache.ws.mongo.MongoDBFields.F_SINGLE_VISITOR_AGGS;
import static com.cache.ws.mongo.MongoDBFields.F_TVT;
import static com.cache.ws.mongo.MongoDBFields.F_UV;
import static com.cache.ws.mongo.MongoDBFields.F_UV_FILTER;
import static com.cache.ws.mongo.MongoDBFields.F_VC;

import java.util.List;

import com.cache.ws.mongo.dto.ResultData;
import com.mongodb.DBObject;

public class DBObjectToResultData {
	private DBObjectToResultData() {
	}

	public static ResultData convert(List<DBObject> _list) {
		ResultData _rd = new ResultData();
		for (DBObject dbObject : _list) {
			_rd.setPv(_rd.getPv() + getValue(dbObject, F_PV));
			_rd.setVc(_rd.getVc() + getValue(dbObject, F_VC));
			_rd.setUv(_rd.getUv() + getValue(dbObject, F_UV));
			_rd.setNew_visitor_aggs(_rd.getNew_visitor_aggs()
					+ getValue(dbObject, F_NEW_VISITOR_AGGS));
			_rd.setUv_filter(_rd.getUv_filter()
					+ getValue(dbObject, F_UV_FILTER));
			_rd.setIp(_rd.getIp() + getValue(dbObject, F_IP));
			_rd.setNuv(_rd.getNuv() + getValue(dbObject, F_NUV));
			_rd.setSingle_visitor_aggs(_rd.getSingle_visitor_aggs()
					+ getValue(dbObject, F_SINGLE_VISITOR_AGGS));
			_rd.setTvt(_rd.getTvt() + getValue(dbObject, F_TVT));
		}

		return _rd;
	}

	private static int getValue(DBObject dbObject, String key) {
		try {
			return Integer.parseInt(dbObject.get(key).toString());
		} catch (Exception e) {
			return 0;
		}
	}

}
