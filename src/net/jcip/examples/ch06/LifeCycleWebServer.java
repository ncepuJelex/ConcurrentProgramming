package net.jcip.examples.ch06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 取消任务，执行周期流程演示
 * @author zhenhua
 * @date 2017年8月16日
 */
public class LifeCycleWebServer {

	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	public void start() throws IOException {
		ServerSocket socket = new ServerSocket(80);
		while(!exec.isShutdown()) {
			try {
				final Socket connection = socket.accept();
				
				exec.execute(new Runnable() {
					@Override
					public void run() {
						handleRequest(connection);
					}
				});
			} catch(RejectedExecutionException e) {
				if(!exec.isShutdown()) {
					log("task submission rejected...",e);
				}
			}
		}
	}
	
	private void log(String string,Exception e) {
		Logger.getAnonymousLogger().log(Level.WARNING, string,e);
	}

	public void handleRequest(Socket connection) {
		Request req = readRequest(connection);
		if(isShutDownRequest(req)) {
			stop();
		} else {
			dispatchRequest(req);
		}
	}
	
	private void dispatchRequest(Request req) {
		
	}

	public void stop() {
		exec.shutdown();
	}

	private boolean isShutDownRequest(Request req) {
		return false;
	}

	private Request readRequest(Socket connection) {
		return null;
	}

	interface Request {
		
	}
}
