package org.hliu.guice.jdbc;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

/**
 * This is the wrapper of data source so that we can use transaction to get connection
 * if a transaction is started for the thread 
 */
public class JdbcDataSource implements DataSource {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(JdbcDataSource.class);
	
	private DataSource dataSource;

	public JdbcDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		
		return dataSource.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		
		return dataSource.getParentLogger();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.dataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.dataSource.setLoginTimeout(seconds);
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return this.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return this.unwrap(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		
		JdbcTransaction transaction = JdbcTransactionHolder.getCurrentTransaction();
		if(transaction != null && transaction.isActive()) {
			
			// we get the connection from the transaction
			logger.debug("Transaction exists for the thread: {}.", Thread.currentThread());
			return transaction.getConnection();
		}
		
		// Get the connection from the connection holder
		/*Connection conn = JdbcConnectionHolder.getCurrentConnection();
		if(conn != null && !conn.isClosed()) {
			logger.debug("Get the existing connection from current thread.");
			return conn;
		}
		*/
		Connection conn = null;
		
		logger.debug("Try to get a new connection");
		conn = this.dataSource.getConnection();
		//JdbcConnectionHolder.setCurrentConnection(conn);
		logger.debug("Got a new connection");
		conn.setAutoCommit(false);
		
		return conn;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		JdbcTransaction transaction = JdbcTransactionHolder.getCurrentTransaction();
		if(transaction != null) {
			// we get the connection from the transaction
			logger.debug("Transaction exists for the thread: {}.", Thread.currentThread());
			return transaction.getConnection();
		}
		
		return this.dataSource.getConnection(username, password);
	}
}
