package org.hliu.guice.jdbc;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class SessionProvider implements Provider<Session> {

	private DataSource dataSource;
	
	@Inject
	public SessionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public Session get() {
		return new JdbcSession(dataSource);
	}

}
