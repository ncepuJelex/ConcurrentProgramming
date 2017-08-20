package net.jcip.examples.ch06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 单个线程在处理，哎！
 * @author zhenhua
 * @date 2017年8月16日
 */
public class SingleThreadWebServer {

	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(80);
		while(true) {
			Socket connection = socket.accept();
			handleRequest(connection);
		}
	}

	private static void handleRequest(Socket connection) {
		// request-handling logic here
	}
}
