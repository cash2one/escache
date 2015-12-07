package com.cache.ws.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cache.ws.constant.ExitConstant;
import com.cache.ws.exit.dto.ExitCountQueryDto;
import com.cache.ws.exit.service.ExitCountService;
import com.cache.ws.util.ExitCountUtils;

@Controller
@Path("/exitCount")
public class ExitCountController {

	@Autowired
	private ExitCountService exitCountService;

	/**
	 * 
	 * @param type
	 *            客户ID
	 * @param rf_type
	 *            来源过滤
	 * @param se
	 *            搜索引擎
	 * @param isNew
	 *            新老访客
	 * @param dateRange
	 *            时间范围
	 * @return
	 */
	@GET
	@Path("/condition/{type}/{rf_type}/{se}/{isNew}/{start}/{end}")
	@Produces("application/json")
	public Map<String, Object> GroupAnalyticsQuery(
			@PathParam("type") String type,
			@PathParam("rf_type") String rfType, @PathParam("se") String se,
			@PathParam("isNew") String isNew,
			@PathParam("start") int start,
			@PathParam("end") int end
			) {
		Map<String, Object> exitCounts = null;

		try {
			
			type = "564d8b584c59da027cba765e818cc246";
			
			if (StringUtils.isBlank(type)) {
				return exitCounts;
			}
			ExitCountQueryDto queryDto = getQueryDto(type, rfType, se, isNew,
					start,end);

			exitCounts = exitCountService.queryExitCount(queryDto);
		} catch (Exception e) {
			return exitCounts;
		}
		return exitCounts;
	}

	private ExitCountQueryDto getQueryDto(String type, String rfType,
			String se, String isNew,int start,int end) {

		ExitCountQueryDto query = new ExitCountQueryDto();

		String seValue = ExitConstant.browsersKeyMap.get(se);
		seValue = seValue == null ? "-1" : seValue;
		query.setType(type);
		query.setRf_type(rfType);
		query.setSe(seValue);
		query.setIsNew(isNew);

		// 获取查询日期
		List<String> tables = ExitCountUtils.getTables(start,end);

		query.setTables(tables);

		return query;
	}

}
