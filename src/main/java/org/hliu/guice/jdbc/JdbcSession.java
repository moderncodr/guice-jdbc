package org.hliu.guice.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The JDBC session implementation which is based on DataSource.
 * 
 */
public class JdbcSession implements Session {

	private DataSource dataSource;
	
	private Logger logger = LoggerFactory.getLogger(JdbcSession.class);
	
	public JdbcSession(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public Connection getConnection() {
		
		Connection conn;
		try {
			conn = this.dataSource.getConnection();
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return conn;
	}

	@Override
	public void close() {
		try {
			this.dataSource.getConnection().close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int execute(String sql) {
		int count;
		Connection conn = null;
		PreparedStatement stat = null;
		
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			logger.debug(stat.toString());
			count = stat.executeUpdate();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return count;
	}
	
	@Override
	public long executeQuery(String sql) {
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		long result = 0;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			rs.next();
			result = rs.getLong(1);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	@Override
	public int execute(String sql, StatementParameterSetter parameterSetter) {
		
		int count;
		Connection conn = null;
		PreparedStatement stat = null;
		
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			count = stat.executeUpdate();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return count;
	}

	@Override
	public long executeQuery(String sql, StatementParameterSetter parameterSetter) {
		
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		long result = 0;
		
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			rs.next();
			result = rs.getLong(1);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return result;
	}
	
	@Override
	public <T> List<T> query(String sql, RecordMapper<T> mapper) {
		
		final List<T> result = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			while(rs.next()) {
				// Store the result
				result.add(mapper.map(rs));
			}
			
			
		} catch (Exception e) {
			logger.warn("Failed to execute sql {}.", sql, e);
			throw new RuntimeException(e);
		}
		finally {
			
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		return result;
	}

	@Override
	public <T> List<T> query(String sql, StatementParameterSetter parameterSetter,
			RecordMapper<T> mapper) {
		
		final List<T> result = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			while(rs.next()) {
				// Store the result
				result.add(mapper.map(rs));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	@Override
	public List<String> query(String sql, StatementParameterSetter parameterSetter) {
		
		final List<String> result = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			while(rs.next()) {
				// Store the result
				result.add(rs.getString(1));
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public List<Long> queryLong(String sql, StatementParameterSetter parameterSetter) {
		
		final List<Long> result = new ArrayList<Long>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			while(rs.next()) {
				// Store the result
				result.add(rs.getLong(1));
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public <T> T queryOne(String sql, RecordMapper<T> mapper) {
		
		T result = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = mapper.map(rs);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	@Override
	public <T> T queryOne(String sql, StatementParameterSetter parameterSetter,
			RecordMapper<T> mapper) {
		T result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			
			// Set the parameters
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = mapper.map(rs);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return result;
	}

	@Override
	public <T> T queryOne(String sql, String parameter, RecordMapper<T> mapper) {
		
		T result = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setString(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = mapper.map(rs);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	@Override
	public <T> T queryOne(String sql, long parameter, RecordMapper<T> mapper) {
		
		T result = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = mapper.map(rs);
			}
			
		} catch (Exception e) {
			logger.warn("Failed to execute sql: {}.", sql);
			throw new RuntimeException(e);
		}
		finally {
			
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public int execute(String sql, String... parameters) {
		
		int count;
		
		Connection conn = null;
		PreparedStatement stat = null;
		
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				for(int i = 1; i <= parameters.length; i++) {
					stat.setString(i, parameters[i - 1]);
				}
				
			}
			logger.debug(stat.toString());
			count = stat.executeUpdate();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return count;
	}
	
	
	@Override
	public int execute(String sql, long parameter) {
		
		int count;
		
		Connection conn = null;
		PreparedStatement stat = null;
		
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			count = stat.executeUpdate();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return count;
	}
	
	@Override
	public long executeQuery(String sql, String... parameters) {
		
		ResultSet rs = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		long result = 0;
		
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				for(int i = 1; i <= parameters.length; i++) {
					stat.setString(i, parameters[i - 1]);
				}
				
			}

			logger.debug(stat.toString());
			rs = stat.executeQuery();
			rs.next();
			result = rs.getLong(1);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return result;
	}

	@Override
	public List<String> query(String sql, String... parameters) {
		
		final List<String> result = new ArrayList<String>();
		PreparedStatement stat = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				for(int i = 1; i <= parameters.length; i++) {
					stat.setString(i, parameters[i - 1]);
				}
				
			}

			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			while(rs.next()) {
				// Store the result
				result.add(rs.getString(1));
			}
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	
	@Override
	public List<String> query(String sql, long parameter) {
		
		final List<String> result = new ArrayList<String>();
		PreparedStatement stat = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			while(rs.next()) {
				// Store the result
				result.add(rs.getString(1));
			}
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public String queryOneString(String sql, String... parameters) {
		
		String result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				for(int i = 1; i <= parameters.length; i++) {
					stat.setString(i, parameters[i - 1]);
				}
			}
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			if(rs.next()) {
				result = rs.getString(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return result;
	}

	@Override
	public String queryOneString(String sql, long id) {
		
		String result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			
			stat.setLong(1, id);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = rs.getString(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return result;
	}
	
	@Override
	public Integer queryOneInt(String sql, String... parameters) {
		Integer result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				for(int i = 1; i <= parameters.length; i++) {
					stat.setString(i, parameters[i - 1]);
				}
				
			}
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	
	@Override
	public Long queryOneLong(String sql, String... parameters) {
		Long result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				for(int i = 1; i <= parameters.length; i++) {
					stat.setString(i, parameters[i - 1]);
				}
				
			}
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			if(rs.next()) {
				result = rs.getLong(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	@Override
	public Integer queryOneInt(String sql, StatementParameterSetter parameterSetter) {
		Integer result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public Long queryOneLong(String sql, StatementParameterSetter parameterSetter) {
		Long result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			if(rs.next()) {
				result = rs.getLong(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public Integer queryOneInt(String sql, long parameter) {
		Integer result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public LocalDate queryOneDate(String sql, long parameter) {
		LocalDate result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = rs.getTimestamp(1).toLocalDateTime().toLocalDate();
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public Long queryOneLong(String sql, long parameter) {
		Long result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			if(rs.next()) {
				result = rs.getLong(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}
	
	@Override
	public LocalDate queryOneDate(String sql, String... parameters) {
		LocalDate result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				for(int i = 1; i <= parameters.length; i++) {
					stat.setString(i, parameters[i - 1]);
				}
			}

			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = rs.getTimestamp(1).toLocalDateTime().toLocalDate();
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		
		return result;
	}

	@Override
	public <T> List<T> query(String sql, String parameter,
			RecordMapper<T> mapper) {
		
		final List<T> result = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setString(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			while(rs.next()) {
				// Store the result
				result.add(mapper.map(rs));
			}
		} catch (Exception e) {
			logger.warn("Failed to execute sql {}.", sql);
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		return result;
	}

	@Override
	public <T> List<T> query(String sql, int parameter, RecordMapper<T> mapper) {
		
		final List<T> result = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setInt(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			while(rs.next()) {
				// Store the result
				result.add(mapper.map(rs));
			}
		} catch (Exception e) {
			logger.warn("Failed to execute sql {}.", sql);
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		return result;
	}

	
	@Override
	public <T> List<T> query(String sql, long parameter, RecordMapper<T> mapper) {
		
		final List<T> result = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			while(rs.next()) {
				// Store the result
				result.add(mapper.map(rs));
			}
		} catch (Exception e) {
			logger.warn("Failed to execute sql {}.", sql);
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		return result;
	}
	@Override
	public String queryOneString(String sql, StatementParameterSetter parameterSetter) {

		String result = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			parameterSetter.setValues(stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			if(rs.next()) {
				result = rs.getString(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	@Override
	public Integer queryOneInt(String sql, List<Parameter> parameters) {
Integer result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			if(parameters != null) {
				setParameters(parameters, stat);
			}
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

	@Override
	public <T> List<T> query(String sql, List<Parameter> parameters, RecordMapper<T> mapper) {
		final List<T> result = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			setParameters(parameters, stat);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			while(rs.next()) {
				// Store the result
				result.add(mapper.map(rs));
			}
		} catch (Exception e) {
			logger.warn("Failed to execute sql {}.", sql);
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		return result;
	}

	private void setParameters(List<Parameter> parameters, PreparedStatement stat) throws SQLException {
		for (int i = 0; i < parameters.size(); i++) {
			setParameter(stat, i + 1, parameters.get(i));
		}
	}

	private void setParameter(PreparedStatement stat, int index, Parameter parameter) throws SQLException {
		if (parameter.getType() == ParameterType.STRING) {
			stat.setString(index, (String)parameter.getValue());
		} else if (parameter.getType() == ParameterType.LONG) {
			if (parameter.getValue() != null) {
				stat.setLong(index, (Long)parameter.getValue());
			} else {
				stat.setNull(index, Types.BIGINT);
			}
		} else if (parameter.getType() == ParameterType.INT) {
			stat.setLong(index, (Integer)parameter.getValue());
		} else if (parameter.getType() == ParameterType.TIMESTAMP) {
			stat.setTimestamp(index, Timestamp.from(Instant.from((LocalDate) parameter.getValue())));
		} else {
			throw new RuntimeException("Parameter is not supported: " + parameter);
		}
	}

	@Override
	public Float queryOneFloat(String sql, long parameter) {
		Float result = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setLong(1, parameter);
			logger.debug(stat.toString());
			rs = stat.executeQuery();
			
			if(rs.next()) {
				result = rs.getFloat(1);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// If the datasource doesn't have a transaction, then close the connection
			try {
				if(rs != null) {
					rs.close();
				}
				if(stat != null) {
					stat.close();
				}
				
				if(conn != null && !JdbcTransactionHolder.hasTransaction()) {
					conn.close();
				}
			}
			catch(Throwable ex) {
				logger.warn("Error in session!", ex);
			}
		}
		
		return result;
	}

}
