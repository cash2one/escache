package com.cache.ws.schedule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cache.ws.mongo.MongoDBOperate;
import com.cache.ws.schedule.GroupAnalyticsScheduleService;
import com.cache.ws.util.GaDateUtils;


	
@Component  
public class GroupAnalyticsScheduleServiceImpl implements GroupAnalyticsScheduleService {  
		
	@Autowired
	private MongoDBOperate mongoDBOperate;
	
	
	 
	@Scheduled(cron="0 0 0 * * ?")
	@Override
	public void exceute() {
	    	String tableName = "ga-"+GaDateUtils.getCurrentDate();

	    	mongoDBOperate.createMongoTable(tableName);

	} 
	    
	   
	      
	}  

