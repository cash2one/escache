package com.cache.ws.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.DBObject;

public class GaUtils {

	public static String[] getIds(List<DBObject> dbObjects) {
		if (dbObjects == null) {
			return null;
		}
		String[] result = new String[dbObjects.size()];
		int i = 0;
		for (DBObject object : dbObjects) {
			result[i] = object.get("userId").toString();
			i++;
		}

		return result;
	}

	public static String getPv(ConcurrentHashMap<String, Integer> pvDataMap,
			Set<String> keys) {
		if (keys == null) {
			return null;
		}
		Integer totalPv = 0;
		for (String key : keys) {
			Integer pv = pvDataMap.get(key) == null ? 0:pvDataMap.get(key);
			totalPv = Integer.sum(totalPv, pv);
		}

		return String.valueOf(totalPv);
	}

	public static ConcurrentHashMap<String, Integer> getPvMap(
			List<DBObject> dbObjects) {
		if (dbObjects == null) {
			return null;
		}

		ConcurrentHashMap<String, Integer> pvDataMap = new ConcurrentHashMap<String, Integer>();

		for (DBObject object : dbObjects) {
			String key = object.get("userId").toString();
			Integer value = Integer.valueOf(object.get("pv").toString());
			pvDataMap.put(key, value);
		}

		return pvDataMap;
	}

	/**
	 * 
	 * 
	 * @param DBObjects
	 * @return
	 */
	public static String calculateRetentionRate(int dividend, int divisor) {

		DecimalFormat df1 = new DecimalFormat("0.00%");

		if(dividend == 0) {
			return "0.00%";
		}
		
		String result = df1.format(Double.valueOf(dividend)
				/ Double.valueOf(divisor));

		return result;
	}
	
	public static Double calculateRetentionRateValue(int dividend, int divisor) {

		if(dividend == 0) {
			return 0.00;
		}
		return Double.valueOf(dividend)
				/ Double.valueOf(divisor);

	}

	public static String calculateRetentionRate(double dayDateRetention,
			int size) {

		DecimalFormat df1 = new DecimalFormat("0.00%");

		String result = df1.format(dayDateRetention / Double.valueOf(size));

		return result;
	}

	public static Double changeString(String str) throws ParseException {
		NumberFormat nf = NumberFormat.getPercentInstance();
		Number m = nf.parse(str);
		return m.doubleValue();
	}

	/**
	 * 
	 * 
	 * @param DBObjects
	 * @return
	 */
	public static int calculateNewUserNumber(List<DBObject> dBObjects) {
		int newCount = 0;
		for (DBObject dBObject : dBObjects) {
			// 新用户
			if (dBObject.get("isNew").equals("0")) {
				newCount++;
			}
		}

		return newCount;
	}

}
