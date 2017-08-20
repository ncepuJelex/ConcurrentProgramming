package net.jcip.examples.ch07;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Encapsulating nonstandard cancellation in a thread by overriding interrupt
 * 通过关闭socket来取消，因为close socket后会引起一序列反应，
 * 比如throws SocketException,然后间接地达到取消线程的目的。
 * @author zhenhua
 *
 */
public class ReaderThread extends Thread {

	private static final int BUFSZ = 512;
	private final Socket socket;
	private final InputStream in;

	public ReaderThread(Socket socket) throws IOException {
		this.socket = socket;
		this.in = socket.getInputStream();
	}

	public void interrupt() {
		try {
			socket.close();
		} catch (IOException ignored) {
		} finally {
			super.interrupt();
		}
	}

	public void run() {
		try {
			byte[] buf = new byte[BUFSZ];
			while (true) {
				int count = in.read(buf);
				if (count < 0) {
					break;
				} else if (count > 0) {
					processBuf(buf, count);
				}
			}
		} catch (IOException e) {
			/*
			 * allow thread to exit
			 */
		}
	}

	public void processBuf(byte[] buf, int count) {

	}
}
