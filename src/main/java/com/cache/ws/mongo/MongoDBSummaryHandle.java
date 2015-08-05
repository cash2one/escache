package com.cache.ws.mongo;

import com.cache.ws.mongo.dto.ResultData;

/**
 * 统计数据处理方法
 * 
 * @author hydm
 *
 */
public class MongoDBSummaryHandle extends MongoDBAbstractHandle {

	@Override
	protected void handleData() {

		for (String _key : keys) {
			ResultData _rd = DBObjectToResultData.convert(key_list.get(_key));
			_rd.setKey_as_string(_key);
			resultDatas.add(_rd);
		}

	}

}
