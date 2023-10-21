package org.hliu.guice.jdbc;


import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

/**
 * Session 
 */
public interface Session {

	/**
	 * Return the connection 
	 */
	public Connection getConnection();
	
	public void close();
	
	/**
	 * Execcute a single SQL statement, e.g. insert, update or delete statements
	 * 
	 * @param sql the SQL statement
	 */
	public int execute(String sql);
	
	public long executeQuery(String sql);
	/**
	 * Execcute a single SQL statement with the parameter setter, e.g. insert, update or delete statements
	 * the parameter setter will be called to set the parameters in the given prepared SQL statement.
	 * @param sql the SQL statement
	 */
	public int execute(String sql, StatementParameterSetter parameterSetter);
	
	public long executeQuery(String sql, StatementParameterSetter parameterSetter);
	
	
	/**
	 * Execute a singel SQL statement with string parameters 
	 */
	public int execute(String sql, String... parameters);
	
	/**
	 * Execute a singel SQL statement with long parameters 
	 */
	public int execute(String sql, long parameter);
	
	public long executeQuery(String sql, String... parameters);
	
	/**
	 * Execute a SQL query(select) statement to get one record. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> T queryOne(String sql, RecordMapper<T> mapper);
	
	public <T> T queryOne(String sql, StatementParameterSetter parameterSetter, RecordMapper<T> mapper);

	/**
	 * Execute a SQL query(select) statement to get one record with one parameter. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> T queryOne(String sql, String parameter, RecordMapper<T> mapper);

	/**
	 * Execute a SQL query(select) statement to get one record with one parameter. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> T queryOne(String sql, long parameter, RecordMapper<T> mapper);
	
	/**
	 * Execute a SQL select statement to get a String result
	 */
	public String queryOneString(String sql, String... parameters);
	
	/**
	 * Execute a SQL select statement to get a String result
	 */
	public String queryOneString(String sql, long id);
	
	/**
	 * Execute a SQL select statement to get a String result
	 */
	public String queryOneString(String sql, StatementParameterSetter parameterSetter);
	
	
	/**
	 * Execute a SQL select statement to get a int result
	 */
	public Integer queryOneInt(String sql, String... parameters);
	
	/**
	 * Execute a SQL select statement to get a int result
	 */
	public Integer queryOneInt(String sql, List<Parameter> parameters);
	
	/**
	 * Execute a SQL select statement to get a int result
	 */
	public Integer queryOneInt(String sql, StatementParameterSetter parameterSetter);
	
	/**
	 * Execute a SQL select statement to get a int result
	 */
	public Integer queryOneInt(String sql, long parameter);
	
	
	/**
	 * Execute a SQL select statement to get a int result
	 */
	public LocalDate queryOneDate(String sql, long param);
	
	public LocalDate queryOneDate(String sql, String... parameters);
	
	/**
	 * Execute a SQL query(select) statement to get one record. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> List<T> query(String sql, RecordMapper<T> mapper);
	
	/**
	 * Execute a SQL query(select) statement to get records with one parameter. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> List<T> query(String sql, String parameter, RecordMapper<T> mapper);
	
	/**
	 * Execute a SQL query(select) statement to get records with one parameter. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> List<T> query(String sql, List<Parameter> parameters, RecordMapper<T> mapper);
	
	/**
	 * Execute a SQL query(select) statement to get records with one int parameter. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> List<T> query(String sql, long parameter, RecordMapper<T> mapper);
	
	/**
	 * Execute a SQL query(select) statement to get records with one int parameter. The record mapper object will be called to map the
	 * record to the object. 
	 */
	public <T> List<T> query(String sql, int parameter, RecordMapper<T> mapper);
	
	public <T> List<T> query(String sql, StatementParameterSetter parameterSetter, RecordMapper<T> mapper);

	public List<String> query(String sql, StatementParameterSetter parameterSetter);
	
	public List<Long> queryLong(String sql, StatementParameterSetter parameterSetter);

	/**
	 * Execute a SQL select statement to get a list of records which has only one field
	 */
	public List<String> query(String sql, String... parameters);
	
	/**
	 * Execute a SQL select statement to get a list of records which has only one field
	 */
	public List<String> query(String sql, long parameter);

	public Long queryOneLong(String sql, String... parameters);
	
	/**
	 * Execute a SQL select statement to get a int result
	 */
	public Long queryOneLong(String sql, long parameter);
	
	public Float queryOneFloat(String sql, long parameter);

	public Long queryOneLong(String sql, StatementParameterSetter setter);

}
