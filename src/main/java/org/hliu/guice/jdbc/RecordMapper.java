package org.hliu.guice.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * the interface used to map one DB record to a object 
 */
public interface RecordMapper<T> {

	public T map(ResultSet rs) throws SQLException;
}
