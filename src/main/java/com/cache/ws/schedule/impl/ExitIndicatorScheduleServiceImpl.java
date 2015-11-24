package com.cache.ws.schedule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cache.ws.constant.ExitConstant;
import com.cache.ws.mongo.MongoDBOperate;
import com.cache.ws.schedule.ExitIndicatorScheduleService;
import com.cache.ws.util.GaDateUtils;

@Component
public class ExitIndicatorScheduleServiceImpl implements
		ExitIndicatorScheduleService {

	@Autowired
	private MongoDBOperate mongoDBOperate;

	@Scheduled(cron = "0 0 0 * * ?")
	@Override
	public void exceute() {

		String tableName = ExitConstant.MONGODB_NAME_EXIT
				+ GaDateUtils.getCurrentDate();
		mongoDBOperate.createMongoTable(ExitConstant.DB_NAME, tableName);

	}

}
