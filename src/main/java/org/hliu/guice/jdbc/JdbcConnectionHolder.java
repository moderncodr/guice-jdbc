package org.hliu.guice.jdbc;

import java.sql.Connection;



public class JdbcConnectionHolder {

	private static ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();
	
	public static void setCurrentConnection(Connection transaction) {
		currentConnection.set(transaction);
	}
	
	public static Connection getCurrentConnection() {
		return currentConnection.get();
	}
	
	public static void removeCurrentConnection() {
		currentConnection.remove();
	}
	
	/**
	 * Tell if a connection has been started in the current thread. 
	 */
	public static boolean hasConnection() {
		
		return currentConnection.get() != null;
	}
}
