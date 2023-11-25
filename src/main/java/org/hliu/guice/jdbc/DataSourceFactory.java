package org.hliu.guice.jdbc;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);
	
	public static DataSource getDataSource(JdbcConfig config) {
		logger.info("Create data source based on pool: {}, ", config.getPool());
		if (JdbcConfig.C3P0_POOL.equals(config.getPool())) {
			return createC3P0DataSource(config);
		}
		if (JdbcConfig.HIKARICP_POOL.equals(config.getPool())) {
			return createHikaricpDataSource(config);
		}
		
		throw new RuntimeException("Pool (" + config.getPool() + ") is not supported!");
	}

	private static DataSource createHikaricpDataSource(JdbcConfig config) {
		HikariDataSource ds = new HikariDataSource();
		try {
			ds.setDriverClassName(config.getDriver());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        
		ds.setJdbcUrl(config.getUrl());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword() );
        ds.setConnectionTimeout(300);
        ds.setMaxLifetime(60);
        ds.setMaximumPoolSize(config.getMaxPoolSize());
        ds.setAutoCommit(config.getAutoCommit());
        return ds;
	}

	public static DataSource createC3P0DataSource(JdbcConfig config) {
		ComboPooledDataSource ds = new ComboPooledDataSource();
    	
    		try {
			ds.setDriverClass(config.getDriver());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        
		ds.setJdbcUrl(config.getUrl());
        ds.setUser(config.getUsername());
        ds.setPassword(config.getPassword() );
        ds.setMinPoolSize(config.getMinPoolSize());
        ds.setAcquireIncrement(5);
        ds.setUnreturnedConnectionTimeout(300);
        ds.setMaxIdleTime(60);
        ds.setMaxConnectionAge(60);
        
        ds.setMaxPoolSize(config.getMaxPoolSize());
        ds.setMaxStatements(config.getMaxStatementSize());
        ds.setAutoCommitOnClose(config.getAutoCommit());
        
        return ds;
	}
}
