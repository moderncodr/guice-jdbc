package org.hliu.guice.jdbc;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import javax.sql.DataSource;

public class SessionProvider implements Provider<Session> {

    private final DataSource dataSource;

    @Inject
    public SessionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Session get() {
        return new JdbcSession(dataSource);
    }

}
