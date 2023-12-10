package org.hliu.guice.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.AgroalDataSourceConfiguration;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.security.NamePrincipal;
import io.agroal.api.security.SimplePassword;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.time.Duration;

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
        if (JdbcConfig.AGROAL.equals(config.getPool())) {
            return createAgroalDataSource(config);
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
        ds.setPassword(config.getPassword());
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
        ds.setPassword(config.getPassword());
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

    public static DataSource createAgroalDataSource(JdbcConfig config) {
        AgroalDataSourceConfiguration dataSourceConfig = new AgroalDataSourceConfigurationSupplier()
                .dataSourceImplementation(AgroalDataSourceConfiguration.DataSourceImplementation.AGROAL)
                .metricsEnabled(false)
                .connectionPoolConfiguration(cp ->
                        cp.validationTimeout(Duration.ofMillis(30_000L))
                                .minSize(config.getMinPoolSize())
                                .maxSize(config.getMaxPoolSize())
                                .initialSize(config.getMinPoolSize())
                                .acquisitionTimeout(Duration.ofMillis(0))
                                .reapTimeout(Duration.ofMillis(0L))
                                .leakTimeout(Duration.ofMillis(0L))
                                .connectionFactoryConfiguration(cf ->
                                        cf.jdbcUrl(config.getUrl())
                                                .principal(new NamePrincipal(config.getUsername()))
                                                .credential(new SimplePassword(config.getPassword()))
                                                .autoCommit(config.getAutoCommit())
                                )
                )
                .get();

        try {
            return AgroalDataSource.from(dataSourceConfig);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
