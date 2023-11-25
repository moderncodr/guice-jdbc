package org.hliu.guice.jdbc;


import java.sql.Connection;

import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTransactionManager implements TransactionManager {

	private final static Logger logger = LoggerFactory.getLogger(JdbcTransactionManager.class);
	
	@Inject
	private DataSource dataSource;
	
	@Override
	public void begin() throws NotSupportedException, SystemException {
		logger.debug("Start the transaction");
		try {
			JdbcTransaction tran = JdbcTransactionHolder.getCurrentTransaction();
			Connection conn = null;
			if(tran == null || !tran.isActive()) {
				// If the transaction is no more active, we need to set the 
				// connecton with a new one
				conn = dataSource.getConnection();
				tran = new JdbcTransaction(conn);
				// We have to put the connection in the holder so that we can get later
				// from the holder and use it in the same thread
				logger.debug("Save the transaction for thread: {}.", Thread.currentThread());
				JdbcTransactionHolder.setCurrentTransaction(tran);
			}
			else {
				logger.debug("There is already existing transaction for thread: {}.", Thread.currentThread());
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException,
			IllegalStateException, SystemException {
		logger.debug("Commit the transaction");
		try {
			logger.debug("Get the connection for thread: {}.", Thread.currentThread());
			JdbcTransaction transaction = JdbcTransactionHolder.getCurrentTransaction();
			if(transaction != null) {
				transaction.commit();
			}
			else {
				logger.warn("Failed to get the current transaction.");
			}
			
			
			// Remove the current transaction
			JdbcTransactionHolder.removeCurrentTransaction();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getStatus() throws SystemException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Transaction getTransaction() throws SystemException {
		logger.debug("Get transaction.");
		final JdbcTransaction tran = JdbcTransactionHolder.getCurrentTransaction();
		if(tran == null) {
			throw new DBException("No transaction is availble. TransactionManager.begin() is probably not yet called.");
		}
		
		return tran;
	}

	@Override
	public void resume(Transaction arg0) throws InvalidTransactionException,
			IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void rollback() throws IllegalStateException, SecurityException,
			SystemException {
		logger.debug("Rollback the transaction");
		JdbcTransaction transaction = JdbcTransactionHolder.getCurrentTransaction();
		try {
			logger.debug("Get the transaction for thread: {}.", Thread.currentThread());
			transaction.rollback();
			// Remove the current transaction
			JdbcTransactionHolder.removeCurrentTransaction();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void setRollbackOnly() throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransactionTimeout(int arg0) throws SystemException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transaction suspend() throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

}
