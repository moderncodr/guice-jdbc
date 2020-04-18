package org.hliu.guice.jdbc;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementParameterSetter {

	public void setValues(PreparedStatement stat) throws SQLException;
}
