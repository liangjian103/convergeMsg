package com.ctfo.parameter;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ctfo.converge.core.util.ParseUtil;
import com.ctfo.converge.core.util.SocketUtil;

/** 获取终端参数设置 */
public class QueryTerminalParameter {

	private static Logger logger = Logger.getLogger(QueryTerminalParameter.class);

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

	/** 连接MSG客户端 */
	QueryTerminalParameter(String ip, int port, String userType, String userId, String userPW, boolean subscriber) {
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
		String login = "LOGI " + userType + " " + userId + " " + userPW + " " + (subscriber ? ("DM") : "") + " \r\n";
		wirteData(login);
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
				String lineStr = in.readLine();
				if (lineStr != null) {
					// 解析是否是上线指令
					if (ParseUtil.isType5Up(lineStr)) {
						String[] lineArray = lineStr.split(" ");
						String oem_sim = lineArray[2];
						String sim = oem_sim.split("_")[1];
						if (!Cache.mapTerminalCache.containsKey(sim)) {
							// 拼获取终端参数指令，进行下发
							String data = "CAITS 0_00000_00001 " + lineArray[2] + " 0 D_GETP {TYPE:0}\r\n";
							logger.info(data);
							wirteData(data);
							Cache.mapTerminalCache.put(sim, 1);
						}
					}
				}else{
					logger.error("readLine NULL!! ip->" + ip + ":" + port + " LOGI " + userType + " " + userId + " " + userPW );
					System.exit(0);
				}
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
