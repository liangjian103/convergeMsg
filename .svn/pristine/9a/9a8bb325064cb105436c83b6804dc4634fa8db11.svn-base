package test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.ctfo.converge.core.SystemStatement;
import com.ctfo.converge.core.util.SocketUtil;

/**
 * 测试连接客户端类
 * 
 * @author James 2015-1-6 15:39:18
 */
public class TestClient {

	private static Logger logger = Logger.getLogger(TestClient.class);

	public Socket socket;
	public PrintWriter out;
	public BufferedReader in;

	public static void main(String[] args) {
		TestClient status = new TestClient();
		status.reveice();
	}

	public void reveice() {
		long a = 0l;
		try {
			connect();
			wirteData("NOOP\r\n");
			while (true) {
				String str = in.readLine();
				a++;
				System.out.println(str);
				// Thread.sleep(50l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Client reveice count:" + a);
		System.exit(0);
	}

	/**
	 * 建立Socket连接
	 */
	private void connect() throws Exception {
		socket = SocketUtil.createSocket("127.0.0.1", SystemStatement.LOCALHOST_PORT, 18000);
		in = SocketUtil.createReader(socket);
		out = SocketUtil.createWriter(socket);
	}

	/**
	 * 发送数据
	 */
	private void wirteData(String data) {
		out.write(data);
		out.flush();
	}

}
