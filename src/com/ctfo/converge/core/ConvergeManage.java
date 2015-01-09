package com.ctfo.converge.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.ctfo.converge.core.util.SocketUtil;

/**
 * 模拟器服务，管理类
 * 
 * @author James 2014-3-15 1:06:49
 */
public class ConvergeManage {

	private static Logger logger = Logger.getLogger(ConvergeManage.class);

	public static void main(String[] args) {
		ConvergeManage server = new ConvergeManage();
		// 2、监听-服务,管理该服务的启动、停止、查看状态
		server.serverListener();
	}

	// 监听客户端连接
	public void serverListener() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				// 监听管理端口
				ServerSocket listener = null;
				// 打开管理端口
				try {
					listener = new ServerSocket(SystemStatement.LOCALHOST_MANAGE_PORT);
				} catch (Exception e) {
					logger.error("Open Manage port ERROR! " + e.getMessage(), e);
					System.exit(0);
					return;
				}
				while (true) {
					try {
						Socket sock = listener.accept();
						String threadId = sock.getInetAddress().getHostAddress() + ":" + sock.getPort();
						sock.setSoTimeout(1000 * 60 * 60 * 6);
						SerSocket serSocket = new SerSocket(threadId, sock);
						serSocket.start();
					} catch (Exception e) {
						logger.error("serverListener ERROR!", e);
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		logger.info("ConvergeManage serverListener OK! ServerPORT:" + SystemStatement.LOCALHOST_MANAGE_PORT);
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
				int a = 0;
				while (true) {
					a++;
					String s;
					try {
						s = in.readLine();
						if (s != null) {
							if ("status".equals(s)) {
								String str = ServiceQueue.INSTANCE.getQueueStatus();
								wirteData(str);
								wirteData("ConvergeManage>");
							}
							if ("kill".equals(s)) {
								System.exit(0);
							}
						} else {
							if (a >= 3) {
								System.out.println("客户端" + threadId + "退出！");
								break;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				logger.error("网络异常,threadId:" + threadId, e);
			} finally {
				try {
					System.out.println("Quit threadId:" + threadId);
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
				out.write(data);
				out.flush();
			} catch (Exception e) {
				logger.error("out.flush() ERROR!", e);
			}
		}
	}

}
