package com.cache.ws.exit.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cache.ws.constant.ExitConstant;
import com.cache.ws.exit.dao.ExitCountDao;
import com.cache.ws.exit.dto.ExitCountQueryDto;
import com.cache.ws.exit.dto.ExitCountResult;
import com.cache.ws.exit.dto.ExitCountSummaryResult;
import com.cache.ws.util.FastJsonUtils;
import com.mongodb.DBObject;

@Component
public class ExitCountService {

	@Autowired
	private ExitCountDao exitCountDao;

	public Map<String, Object> queryExitCount(ExitCountQueryDto queryDto)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		List<String> tables = queryDto.getTables();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		for (String table : tables) {

			String talbeName = ExitConstant.MONGODB_NAME_EXIT + table;

			DBObject data = exitCountDao.queryExitCount(talbeName,
					queryDto.getType(), queryDto.getIsNew(),
					queryDto.getRf_type(), queryDto.getSe());

			List<ExitCountResult> dataTabe = FastJsonUtils.json2list(
					data.toString(), ExitCountResult.class);

			resultMap = addData(dataTabe, resultMap);

		}
		return resultMap;

	}

	public Map<String, Object> queryExitCountSummary(ExitCountQueryDto queryDto)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		List<String> tables = queryDto.getTables();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		for (String table : tables) {

			String talbeName = ExitConstant.MONGODB_NAME_EXIT + table;

			DBObject data = exitCountDao.queryExitCountSummay(talbeName,
					queryDto.getType(), queryDto.getIsNew(),
					queryDto.getRf_type(), queryDto.getSe());

			List<ExitCountSummaryResult> dataTabe = FastJsonUtils.json2list(
					data.toString(), ExitCountSummaryResult.class);

			addSummaryData(dataTabe, resultMap);

		}
		return resultMap;

	}

	private Map<String, Object> addData(List<ExitCountResult> dataTabe,
			Map<String, Object> result) {

		if (dataTabe != null && dataTabe.size() > 0) {
			for (ExitCountResult data : dataTabe) {

				String key = data.getUrl();
				int value = data.getEc();

				if (result.containsKey(key)) {
					value += Integer.valueOf(result.get(key).toString());
				}
				result.put(key, value);
			}
		}

		return result;

	}

	private Map<String, Object> addSummaryData(
			List<ExitCountSummaryResult> dataTabe, Map<String, Object> result) {

		if (dataTabe != null && dataTabe.size() > 0) {
			for (ExitCountSummaryResult data : dataTabe) {

				int value = data.getEc();
				if (result.containsKey("ecSummary")) {
					value += Integer
							.valueOf(result.get("ecSummary").toString());
				}
				result.put("ecSummary", value);
			}
		}

		return result;

	}

}
