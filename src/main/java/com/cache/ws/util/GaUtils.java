package com.cache.ws.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.ws.constant.GaConstant;
import com.mongodb.DBObject;

public class GaUtils {

	public static String displayDate(String date, String scale)
			throws ParseException {
		if (scale.equals(GaDateUtils.DAY)) {
			return date;
		} else if (scale.equals(GaDateUtils.WEEK)) {
			date += " - " + GaDateUtils.getSpecifiedOffsetWeek(date, 1);

		} else if (scale.equals(GaDateUtils.MONTH)) {
			date += " - " + GaDateUtils.getSpecifiedOffsetMonth(date, 1);
		}

		return date;
	}

	public static List<String> getMongDBName(String scale, List<String> dates) {
		List<String> dbNames = new ArrayList<String>();

		for (String date : dates) {
			if (scale.equals(GaDateUtils.DAY)) {
				date = GaConstant.MONGODB_NAME_DAY + date;
			} else if (scale.equals(GaDateUtils.WEEK)) {
				date = GaConstant.MONGODB_NAME_WEEK + date;
			} else if (scale.equals(GaDateUtils.MONTH)) {
				date = GaConstant.MONGODB_NAME_MONTH + date;
			}

			dbNames.add(date);
		}
		return dbNames;
	}

	public static String[] getIds(List<DBObject> dbObjects) {
		if (dbObjects == null) {
			return new String[] {};
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
		Integer totalPv = 0;
		if (keys == null) {
			String.valueOf(totalPv);
		}

		for (String key : keys) {
			Integer pv = pvDataMap.get(key) == null ? 0 : pvDataMap.get(key);
			totalPv = Integer.sum(totalPv, pv);
		}

		return String.valueOf(totalPv);
	}

	public static ConcurrentHashMap<String, Integer> getPvMap(
			List<DBObject> dbObjects) {
		ConcurrentHashMap<String, Integer> pvDataMap = new ConcurrentHashMap<String, Integer>();

		if (dbObjects == null) {
			return pvDataMap;
		}
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
	public static String calculateRetentionRate(Double dividend, int divisor) {

		DecimalFormat df1 = new DecimalFormat("0.00%");

		if (dividend == 0) {
			return "0.00%";
		}

		String result = df1.format(Double.valueOf(dividend)
				/ Double.valueOf(divisor));

		return result;
	}

	/**
	 * 
	 * 
	 * @param DBObjects
	 * @return
	 */
	public static String calculateRetentionRate(int dividend, int divisor) {

		DecimalFormat df1 = new DecimalFormat("0.00%");

		if (dividend == 0) {
			return "0.00%";
		}

		String result = df1.format(Double.valueOf(dividend)
				/ Double.valueOf(divisor));

		return result;
	}

	public static Double calculateRetentionRateValue(int dividend, int divisor) {

		if (dividend == 0) {
			return 0.00;
		}
		BigDecimal bigDecimal = new BigDecimal(Double.valueOf(dividend)
				/ Double.valueOf(divisor));

		Double value =  bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		return value;

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
