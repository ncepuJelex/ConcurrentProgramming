package net.jcip.examples.ch06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 每次来一个连接，都启动一个线程来处理，太浪费了！
 * @author zhenhua
 * @date 2017年8月16日
 */
public class ThreadPerTaskWebServer {

	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(80);
		while(true) {
			final Socket connection = socket.accept();
			
			Runnable task = new Runnable() {
				@Override
				public void run() {
					handleRequest(connection);
				}
			};
			
			new Thread(task).start();
		}
	}
	
	private static void handleRequest(Socket connection) {
		// request-handling logic goes here
		
	}
}
