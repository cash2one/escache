package com.cache.ws.util;

import java.util.ArrayList;
import java.util.List;

public class ExitCountUtils {

	public static List<String> getTables(int start,int end) {
		List<String> collections = new ArrayList<String>();
		String endDate = GaDateUtils.getCurrentDate();
		if (end == 0 && start == 0) {
			collections.add(endDate);
		} else {
			collections = GaDateUtils.getDaysListBetweenDates(start,end);
		}

		return collections;
	}
}
