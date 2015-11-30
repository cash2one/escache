package com.cache.ws.util;

import java.util.ArrayList;
import java.util.List;

public class ExitCountUtils {

	public static List<String> getTables(int dateRange) {
		List<String> collections = new ArrayList<String>();
		String endDate = GaDateUtils.getCurrentDate();
		if (dateRange == 0) {
			collections.add(endDate);
		} else {
			collections = GaDateUtils.getDaysListBetweenDates(
					Math.abs(dateRange), endDate);
		}

		return collections;
	}
}
