package com.cache.ws.rest;

import java.text.ParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cache.ws.constant.GaConstant;
import com.cache.ws.ga.dto.GaResult;
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
	public GaResult GroupAnalyticsQuery(@PathParam("type") String type,
			@PathParam("scale") String scale,
			@PathParam("dateRange") int dateRange,
			@PathParam("indicator") String indicator) {
		GaResult result = new GaResult();

		if (StringUtils.isBlank(indicator)) {
			return result;
		}

		try {
			if (indicator.equals(GaConstant.PV)) {
				result = groupAnalyticsService.queryPV(type, scale, dateRange);
			} else if (indicator.equals(GaConstant.VISITOR)) {
				result = groupAnalyticsService.queryVisitors(type, scale,
						dateRange);
			} else if (indicator.equals(GaConstant.RETENTION_RATE)) {
				result = groupAnalyticsService.queryRetentionRate(type, scale,
						dateRange);
			}
		} catch (ParseException e) {
			result.setCode(GaConstant.GA_ERROR_CODE);
			return result;
		}

		return result;
	}

}
