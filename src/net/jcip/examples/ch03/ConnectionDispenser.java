package net.jcip.examples.ch03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 使用ThreadLocal来确保线程安全性，
 * 没想到ThreadLocal还有个initialValue()方法！
 * @author zhenhua
 * @date 2017年8月14日
 */
public class ConnectionDispenser {

	static String DB_URL = "jdbc:mysql:localhost:3306/mydatabase";
	
	private ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {

		@Override
		protected Connection initialValue() {
			try {
				return DriverManager.getConnection(DB_URL);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to acquire connection...");
			}
		}
	};
	
	public Connection getConnection() {
		return connectionHolder.get();
	}
}
