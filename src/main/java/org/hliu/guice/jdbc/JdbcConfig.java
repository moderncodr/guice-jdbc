package org.hliu.guice.jdbc;


public class JdbcConfig {

    private boolean autoCommit;

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMaxStatementSize() {
        return maxStatementSize;
    }

    public void setMaxStatementSize(int maxStatementSize) {
        this.maxStatementSize = maxStatementSize;
    }

    private int minPoolSize = 5;

    private int maxPoolSize = 20;

    private int maxStatementSize = 180;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    private String username;

    private String password;

    private String driver;

    private String url;

    private String pool = HIKARICP_POOL;

    public final static String HIKARICP_POOL = "HIKARICP";
    public final static String C3P0_POOL = "c3p0";
    public final static String AGROAL = "agroal";

    public boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
