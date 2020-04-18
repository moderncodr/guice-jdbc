package org.hliu.guice.jdbc;


import java.sql.Connection;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTransaction implements Transaction {

	private Connection conn;
	
	private Logger logger = LoggerFactory.getLogger(JdbcTransaction.class);
	
	private boolean active = true;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public JdbcTransaction(Connection conn) {
		this.conn = conn;
	}

	public Connection getConnection() {
		return this.conn;
	}
	
	@Override
	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException,
			IllegalStateException, SystemException {
		try {
			logger.debug("commit the transaction.");
			this.conn.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			release();
		}
	}

	@Override
	public boolean delistResource(XAResource arg0, int arg1)
			throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean enlistResource(XAResource arg0) throws RollbackException,
			IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getStatus() throws SystemException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registerSynchronization(Synchronization arg0)
			throws RollbackException, IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() throws IllegalStateException, SystemException {
		try {
			this.conn.rollback();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			release();
		}
	}

	@Override
	public void setRollbackOnly() throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		
	}

	private void release() {
		
		try {
			this.conn.close();
		} catch (Exception e) {
			logger.warn("Failed to close the connection!", e);
		}
		
		this.conn = null;
		
		this.active = false;
	}

}
