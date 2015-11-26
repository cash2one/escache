package com.cache.ws.exit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cache.ws.constant.ExitConstant;
import com.cache.ws.exit.dao.ExitCountDao;
import com.cache.ws.exit.dto.ExitCountQueryDto;
import com.cache.ws.exit.dto.ExitCountResult;
import com.mongodb.DBObject;

@Component
public class ExitCountService {

	@Autowired
	private ExitCountDao exitCountDao;

	public List<ExitCountResult> queryExitCount(ExitCountQueryDto queryDto) {

		List<ExitCountResult> result = null;

		List<String> tables = queryDto.getTables();
		List<DBObject> datas = new ArrayList<DBObject>();
		for (String table : tables) {

			String talbeName = ExitConstant.MONGODB_NAME_EXIT + table;

			DBObject data = exitCountDao.queryExitCount(talbeName,
					queryDto.getType(), queryDto.getIsNew(),
					queryDto.getRf_type(), queryDto.getSe());

			datas.add(data);

		}

		return result;

	}

}
