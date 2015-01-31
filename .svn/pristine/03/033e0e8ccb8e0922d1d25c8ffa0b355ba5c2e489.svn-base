package com.ctfo.parameter;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctfo.parameter.dao.QueryTerminalParameterDAO;

/** 定时任务 **/
public class QueryTerminalParameterQuartz {

	private static Logger logger = Logger.getLogger(QueryTerminalParameterQuartz.class);
	
	private QueryTerminalParameterDAO queryTerminalParameterDAO;

	public void setQueryTerminalParameterDAO(QueryTerminalParameterDAO queryTerminalParameterDAO) {
		this.queryTerminalParameterDAO = queryTerminalParameterDAO;
	}

	/** 每天定时加载一次*/
	public void loadCache() {
		try {
			List<Map<String, Object>> list = queryTerminalParameterDAO.queryTB_TERMINAL_PARAM();
			// 清空缓存
			Cache.mapTerminalCache.clear();
			for (Map<String, Object> map : list) {
				Cache.mapTerminalCache.put(map.get("COMMADDR") + "", 0);
			}
			logger.info("loadCache OK!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
