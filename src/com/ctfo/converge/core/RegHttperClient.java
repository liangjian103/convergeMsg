package com.ctfo.converge.core;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ctfo.converge.core.util.FileUtils;
import com.ctfo.converge.core.util.SocketUtil;

/**
 * RegHttper客户端类
 * 
 * @author James 2015-1-6 17:42:52
 */
public class RegHttperClient {

	private static Logger logger = Logger.getLogger(RegHttperClient.class);

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	// RegHttper的IP
	private String ip;
	// RegHttper的端口
	private int port;

	// RegHttper 登录类型 PIPE、WEB、PROXY、SEND、SAVE、SAVE_UP、SAVE_DOWN
	private String userType;
	// RegHttper登录用户名
	private String userId;
	// RegHttper登录密码
	private String userPW;
	// true:订阅，false:非订阅
	private boolean subscriber = false;

	/** 连接RegHttper客户端 */
	RegHttperClient(String ip, int port, String userType, String userId, String userPW, boolean subscriber) {
		this.ip = ip;
		this.port = port;
		this.userType = userType;
		this.userId = userId;
		this.userPW = userPW;
		this.subscriber = subscriber;
	}

	/** 启动RegHttper数据发送 */
	public void start() {
		Runnable r = new Runnable() {
			public void run() {
				// 创建连接
				connect(ip, port, 18000);
				// 登录MSG
				login(userType, userId, userPW);
				// 定时发送心跳保证连接不断
				sendNoopData();
				// 订阅数据
				subscriber();
				// 接收数据
				reveice();
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	/**
	 * 建立Socket连接
	 */
	private void connect(String serverIP, int serverPort, int reConnectTime) {
		try {
			socket = SocketUtil.createSocket(serverIP, serverPort, reConnectTime);
			in = SocketUtil.createReader(socket);
			out = SocketUtil.createWriter(socket);
		} catch (Exception e) {
			logger.error("connect(" + serverIP + ":" + serverPort + ") ERROR!", e);
		}
	}

	/**
	 * 登录MSG
	 * 
	 * @param userType
	 *            : PIPE、WEB、PROXY、SEND、SAVE、SAVE_UP、SAVE_DOWN
	 * */
	private void login(String userType, String userId, String userPW) {
		wirteData("LOGI " + userType + " " + userId + " " + userPW + " " + (subscriber ? ("DM") : "") + " \r\n");
	}

	/** 订阅指令 */
	private void subscriber() {
		// 载入订阅指令
		if (subscriber) {
			List<String> list = FileUtils.loadLineFile(SystemStatement.SUBSCRIPTION_DATA);
			for (String line : list) {
				logger.info("subscriber -> " + line);
				wirteData(line + " \r\n");
			}
		}
	}

	/**
	 * 发送数据
	 */
	private void wirteData(String data) {
		out.write(data);
		out.flush();
	}

	/** 接收数据 */
	private void reveice() {
		try {
			while (true) {
				String str = SystemStatement.REGHTTPER_QUEUE.take();
				logger.info("SEND-REGHTTPER ip->" + ip + ":" + port + " ->" + str);
				// 发给reghttper
				wirteData(str + " \r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("reveice() ip->" + ip + ":" + port + ",subscriber:" + subscriber + " ERROR!", e);
		}
		System.exit(0);
	}

	/** 心跳-定时发送 */
	public void sendNoopData() {
		Runnable r = new Runnable() {
			public void run() {
				wirteData("NOOP \r\n");
			}
		};
		int delay = 1;// 当前时间后1秒开始
		int period = 30;// 间隔时间30秒执行一次
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(r, delay, period, TimeUnit.SECONDS);
	}

}
