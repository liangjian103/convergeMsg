package com.ctfo.converge.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.ctfo.converge.core.util.PropertiesUtil;

/**
 * Title: 平台常量配置类
 * 
 * @author LiangJian
 */
public class SystemStatement {

	/** reghttper数据队列 */
	public static ArrayBlockingQueue<String> REGHTTPER_QUEUE = new ArrayBlockingQueue<String>(100000);
	/** 上线指令 数据队列 */
//	public static ArrayBlockingQueue<String> MSGUP_QUEUE = new ArrayBlockingQueue<String>(100000);

	public static String SYSTEM_PROPERTIES = "system.properties";
	public static String SUBSCRIPTION_DATA = "SUBSCRIPTION_DATA.dat";

	/** 是否为reghttper推送数据 */
	public static boolean reghttper = Boolean.parseBoolean(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "reghttper_send"));

	/** 当前服务管理端口 */
	public static int LOCALHOST_MANAGE_PORT = Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "localhost_manage_port"));

	/** 当前服务数据推送端口 */
	public static int LOCALHOST_PORT = Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "localhost_port"));

	/** MSG总个数 */
	public static int msg_count = Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "msg_count"));

	/** MSG连接配置 */
	public static List<ConfBean> getConfBeanList() {
		List<ConfBean> confBeanList = new ArrayList<ConfBean>();
		for (int i = 1; i <= msg_count; i++) {
			ConfBean confBean = new ConfBean();
			confBean.setIp(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "msg" + i + "_ip"));
			confBean.setPort(Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "msg" + i + "_port")));
			confBean.setUserType(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "msg" + i + "_userType"));
			confBean.setUserId(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "msg" + i + "_userId"));
			confBean.setUserPW(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "msg" + i + "_userPW"));
			confBean.setSubscriber(Boolean.parseBoolean(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "msg" + i + "_subscriber")));
			confBeanList.add(confBean);
		}
		return confBeanList;
	}

	/** reghttper总个数 */
	public static int reghttper_count = Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "reghttper_count"));

	/** reghttper连接配置 */
	public static List<ConfBean> getReghttper() {
		List<ConfBean> confBeanList = new ArrayList<ConfBean>();
		for (int i = 1; i <= reghttper_count; i++) {
			ConfBean confBean = new ConfBean();
			confBean.setIp(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "reghttper" + i + "_ip"));
			confBean.setPort(Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "reghttper" + i + "_port")));
			confBean.setUserType(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "reghttper" + i + "_userType"));
			confBean.setUserId(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "reghttper" + i + "_userId"));
			confBean.setUserPW(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "reghttper" + i + "_userPW"));
			confBean.setSubscriber(false);// 没有订阅概念
			confBeanList.add(confBean);
		}
		return confBeanList;
	}
}
