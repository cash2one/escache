package com.cache.ws.constant;

import java.util.HashMap;
import java.util.Map;

public class ExitConstant {
	
	public static String MONGODB_NAME_EXIT = "exit-indicator-";

	public static String DB_NAME = "exit";
	
	public static Map<String,String> browsersKeyMap = new HashMap<String,String>(5);
	
	static {
		browsersKeyMap.put("1","百度");
		browsersKeyMap.put("2","Google");
		browsersKeyMap.put("3","搜狗");
		browsersKeyMap.put("4","好搜");
		browsersKeyMap.put("5","必应");
		browsersKeyMap.put("6","其他");		
	}
	
	
}
