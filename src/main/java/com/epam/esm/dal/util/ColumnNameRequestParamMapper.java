package com.epam.esm.dal.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.rest.parameter.ParameterConstant;

@Component
public final class ColumnNameRequestParamMapper {
	
	private static final Logger log = LogManager.getLogger(ColumnNameRequestParamMapper.class);
	
	private final Map<String, String> columns = new HashMap<String, String>();

	public ColumnNameRequestParamMapper() {
		
		columns.put(ParameterConstant.TAG, ColumnNameHolder.TAG_NAME);
		columns.put(ParameterConstant.CERTIFICATE_NAME, ColumnNameHolder.CERTIFICATE_NAME);
		columns.put(ParameterConstant.DESCRIPTION, ColumnNameHolder.CERTIFICATE_DESCRIPTION);
		columns.put(ParameterConstant.CREATION_DATE, ColumnNameHolder.CERTIFICATE_CREATION_DATE);
	}
	
	public String getColumnByParamName(String paramName) {
		log.info(columns);
		log.info("%%%%GET_COLUMN: " + paramName);
		log.info("%%%%RESULT_COLUMN: " + columns.get(paramName));
		return columns.get(paramName);
	}

}
