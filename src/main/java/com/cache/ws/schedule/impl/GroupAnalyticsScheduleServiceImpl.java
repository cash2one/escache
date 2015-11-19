package com.cache.ws.schedule.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cache.ws.constant.GaConstant;
import com.cache.ws.mongo.MongoDBOperate;
import com.cache.ws.schedule.GroupAnalyticsScheduleService;
import com.cache.ws.util.GaDateUtils;

@Component
public class GroupAnalyticsScheduleServiceImpl implements
		GroupAnalyticsScheduleService {

	@Autowired
	private MongoDBOperate mongoDBOperate;

	@Scheduled(cron = "0 0 0 * * ?")
	@Override
	public void exceute() {
		Calendar cal = Calendar.getInstance();

		String tableName = GaConstant.MONGODB_NAME_DAY
				+ GaDateUtils.getCurrentDate();
		mongoDBOperate.createMongoTable(GaConstant.DB_NAME, tableName);

		// 判断是否需要建立周表
		boolean isWeek = GaDateUtils.isBeginningOfWeek(cal);

		if (isWeek) {
			tableName = GaConstant.MONGODB_NAME_WEEK
					+ GaDateUtils.getCurrentDate();
			mongoDBOperate.createMongoTable(GaConstant.DB_NAME, tableName);
		}

		// 判断是否需要建立月表
		boolean isMonth = GaDateUtils.isBeginningOfMonth(cal);

		if (isMonth) {
			tableName = GaConstant.MONGODB_NAME_MONTH
					+ GaDateUtils.getCurrentMonth();
			mongoDBOperate.createMongoTable(GaConstant.DB_NAME, tableName);
		}

	}

}
