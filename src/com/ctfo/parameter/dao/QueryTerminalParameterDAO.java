package com.ctfo.parameter.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/** 数据库操作DAO */
public class QueryTerminalParameterDAO {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> queryTB_TERMINAL_PARAM() {
		String sql = "SELECT S.COMMADDR AS COMMADDR FROM TR_SERVICEUNIT U, TB_SIM S, (SELECT T.TID FROM TB_TERMINAL_PARAM T GROUP BY T.TID) AAA WHERE U.TID = AAA.TID AND U.SID = S.SID";
		return jdbcTemplate.queryForList(sql);
	}
}
