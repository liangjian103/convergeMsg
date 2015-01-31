package com.ctfo.parameter;

import java.util.List;

import com.ctfo.converge.core.ConfBean;
import com.ctfo.converge.core.SystemStatement;
import com.ctfo.spring.factory.InitSpring;

public class QueryTerminalParameterMaim {

	public static void main(String[] args) {
		try {
			InitSpring.INSTANCE.getContext();
			// 1、连接MSG，全量或订阅MSG车辆数据
			List<ConfBean> list = SystemStatement.getConfBeanList();
			for (ConfBean confBean : list) {
				QueryTerminalParameter queryTerminalParameter = new QueryTerminalParameter(confBean.getIp(), confBean.getPort(), confBean.getUserType(), confBean.getUserId(), confBean.getUserPW(), confBean.getSubscriber());
				queryTerminalParameter.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
