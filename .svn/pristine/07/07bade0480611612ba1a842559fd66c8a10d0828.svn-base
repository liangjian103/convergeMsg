package com.ctfo.converge.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;

import com.ctfo.converge.core.util.SocketUtil;

/**
 * 模拟器服务，启动类
 * 
 * @author James 2014-3-15 1:06:39
 */
public class ConvergeMain {

	private static Logger logger = Logger.getLogger(ConvergeMain.class);
	private static Logger sendData = Logger.getLogger("sendData");

	public static void main(String[] args) {
		// 1、连接MSG，全量或订阅MSG车辆数据
		List<ConfBean> list = SystemStatement.getConfBeanList();
		for (ConfBean confBean : list) {
			ConvergeClient converge = new ConvergeClient(confBean.getIp(), confBean.getPort(), confBean.getUserType(), confBean.getUserId(), confBean.getUserPW(), confBean.getSubscriber());
			converge.start();
		}

		//1.1、连接regthhper，推送注册指令
		if(SystemStatement.reghttper){
			List<ConfBean> listReghttper = SystemStatement.getReghttper();
			for (ConfBean confBean : listReghttper) {
				RegHttperClient regHttperClient = new RegHttperClient(confBean.getIp(), confBean.getPort(), confBean.getUserType(), confBean.getUserId(), confBean.getUserPW(), confBean.getSubscriber());
				regHttperClient.start();
			}
		}
		
		// 2、监听数据接收客户端
		ConvergeMain server = new ConvergeMain();
		server.serverListener();
		// 3、监听-服务,管理该服务的启动、停止、查看状态
		ConvergeManage serverManage = new ConvergeManage();
		serverManage.serverListener();
		System.out.println("RUN OK!!");
	}

	// 监听客户端连接
	private void serverListener() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				// 监听管理端口
				ServerSocket listener = null;
				// 打开管理端口
				try {
					listener = new ServerSocket(SystemStatement.LOCALHOST_PORT);
				} catch (Exception e) {
					logger.error("打开数据推送端口时错误" + e.getMessage(), e);
					System.out.println("不能启动模拟器服务，服务可能已经启动了");
					System.exit(0);
					return;
				}
				while (true) {
					try {
						Socket sock = listener.accept();
						sock.setSoTimeout(1000 * 60 * 60 * 6);
						String threadId = sock.getInetAddress().getHostAddress() + ":" + sock.getPort();
						SerSocket serSocket = new SerSocket(threadId, sock);
						ServiceQueue.INSTANCE.addQueue(threadId);// 开辟队列
						serSocket.wirteData("LACK 0 0 0 106101");// 存储服务需要一个MSGID
						serSocket.start();
					} catch (Exception e) {
						logger.error("serverListener ERROR!", e);
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		logger.info("ConvergeMain serverListener OK! ServerPORT:" + SystemStatement.LOCALHOST_PORT);
	}

	// 通信线程内部类
	private static class SerSocket extends Thread {
		public String threadId;
		public Socket socket;
		public PrintWriter out;
		public BufferedReader in;

		public SerSocket(String threadId, Socket socket) {
			this.threadId = threadId;
			this.socket = socket;
			out = SocketUtil.createWriter(socket);
			in = SocketUtil.createReader(socket);
		}

		public void run() {
			try {
				// 发数据
				new Thread() {
					@Override
					public void run() {
						while (true) {
							String data = ServiceQueue.INSTANCE.getQueueByData(threadId);
							sendData.info("threadId:" + threadId + " " + data);
							wirteData(data);
						}
					}
				}.start();
				// 接数据
				int count = 0;
				while (true) {
					count++;
					String s = in.readLine();
					System.out.println(s);
					if (s == null) {
						if (count >= 3) {
							// 移除该存储队列
							ServiceQueue.INSTANCE.removeQueue(threadId);
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("连接异常！threadId:" + threadId, e);
				// 移除该存储队列
				ServiceQueue.INSTANCE.removeQueue(threadId);
			} finally {
				try {
					socket.close();
					out.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void wirteData(String data) {
			try {
				out.write(data + "\r\n");
				out.flush();
			} catch (Exception e) {
				logger.error("out.flush() ERROR!", e);
			}
		}
	}

}
