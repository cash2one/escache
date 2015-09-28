package com.cache.ws.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cache.ws.ga.dto.GaResultTrData;
import com.cache.ws.ga.service.GroupAnalyticsService;
import com.cache.ws.mongo.MongoDBOperate;

@Controller
@Path("/groupAnalytics")
public class GroupAnalyticsController {

	@Autowired
	private MongoDBOperate operate;

	@Autowired
	private GroupAnalyticsService groupAnalyticsService;

	/**
	 * 
	 * @param type
	 *            客户ID
	 * @param scale
	 *            选择规模（天，周，月）
	 * @param dateRange
	 *            时间范围
	 * @param indicator
	 *            查询指标
	 * @return
	 */
	@GET
	@Path("/condition/{type}/{scale}/{dateRange}/{indicator}")
	@Produces("application/json")
	public List<GaResultTrData> GroupAnalyticsQuery(
			@PathParam("type") String type, @PathParam("scale") String scale,
			@PathParam("dateRange") int dateRange,
			@PathParam("indicator") String indicator) {
		List<GaResultTrData> result = new ArrayList<GaResultTrData>();

		result = groupAnalyticsService.queryPV(type, scale, dateRange);

		return result;
	}

}
