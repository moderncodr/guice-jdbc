package org.hliu.guice.jdbc;


public class DBException extends RuntimeException {

	/**
	 * The generated serial Version UID
	 */
	private static final long serialVersionUID = -6595054925150515342L;

	public DBException(String message, Exception ex) {
		super(message, ex);
	}
	
	public DBException(Exception ex) {
		super(ex);
	}

	public DBException(String message) {
		super(message);
	}
}
