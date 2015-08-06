package com.cache.ws.mongo;

public interface MongoDBFields {
	// ES
	String F_KEY_AS_STRING = "key_as_string";
	String F_PV = "pv";// 浏览量
	String F_VC = "vc";// 访问次数
	String F_UV = "uv";// 访客数
	String F_OUT_RATE = "outRate";// 跳出率
	String F_IP = "ip";// IP数
	String F_NUV = "nuv";// 新访客数
	String F_NEW_VISITOR_AGGS = "new_visitor_aggs";
	String F_UV_FILTER = "uv_filter";
	String F_SINGLE_VISITOR_AGGS = "single_visitor_aggs";
	String F_NUV_RATE = "nuvRate";// 新访客比率
	String F_ARRIVED_RATE = "arrivedRate";// 抵达率
	String F_AVG_TIME = "avgTime";// 平均访问时长
	String F_AVG_PAGE = "avgPage";// 平均访问页数
	String F_TVT = "tvt";
	String F_OUT_VC_AGGS = "out_vc_aggs";

	// MongoDB
	String F_MODULE_ID = "moduleId";
	String F_TYPE = "type";// 类型。表格或者统计

}
