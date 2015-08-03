package com.cache.ws.util;

import java.util.Date;

public class CacheUtils {

	@SuppressWarnings("deprecation")
	public static String[] createIndexes(Long startDay, Long endDay,
			String prefix) {
		
		if(startDay == null || endDay == null) {
			new NullPointerException("CacheUtils.CreateIndexes: Please check startDay or endDay." );
		}
		
		int dayMills = 24 * 60 * 60 * 1000;

		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setSeconds(0);

		Long startTime = date.getTime();

		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		date.setSeconds(999);

		Long endTime = date.getTime();

		startTime = startTime + startDay * dayMills;
		endTime = endTime + endDay * dayMills;

		Double number = Math.floor((endTime - startTime) / dayMills);

		String[] dates = new String[number.intValue()];
		Date start = new Date(startTime);
		for (int i = 0; i < number; i++) {

			if (i == 0) {
				start.setDate(start.getDate());
			} else {
				start.setDate(start.getDate() + 1);
			}

			dates[i] = (prefix + String.format("%tF%n", start));
		}

		return dates;
	}

	public static void main(String[] args) {
		String[] test = createIndexes(new Long(0), new Long(0), "access-");

		for (String s : test) {
			System.out.println(s);
		}
	}
}
