package org.hliu.guice.jdbc;



public class JdbcTransactionHolder {

	private static ThreadLocal<JdbcTransaction> currentTransaction = new ThreadLocal<JdbcTransaction>();
	
	
	public static void setCurrentTransaction(JdbcTransaction transaction) {
		currentTransaction.set(transaction);
	}
	
	public static JdbcTransaction getCurrentTransaction() {
		return currentTransaction.get();
	}
	
	public static void removeCurrentTransaction() {
		currentTransaction.remove();
	}
	
	/**
	 * Tell if a transaction has been started in the current thread. 
	 */
	public static boolean hasTransaction() {
		
		return currentTransaction.get() != null;
	}
}
