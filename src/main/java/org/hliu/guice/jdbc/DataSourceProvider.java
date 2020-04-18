package org.hliu.guice.jdbc;


import java.util.Properties;

import javax.sql.DataSource;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.google.inject.Provider;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourceProvider implements Provider<DataSource> {
    
	private static final Logger logger = LoggerFactory.getLogger(DataSourceProvider.class);
	
    private DataSource dataSource;
    
    
    public DataSourceProvider() {

    		JdbcConfig config = getConfig();
    		DataSource ds = DataSourceFactory.getDataSource(config);    
        this.dataSource = new JdbcDataSource(ds);
    }
	
	
	private DataSource getDataSource(JdbcConfig config) {
		
		return null;
	}

	

	private JdbcConfig getConfig() {
		
		JdbcConfig config = new JdbcConfig();
		Properties prop = new Properties();
		try {
		    //load a properties file from class path, inside static method
		    prop.load(JdbcConfig.class.getResourceAsStream("/database.properties"));

		    //get the property value and print it out
		    config.setDriver(prop.getProperty("driver"));
		    config.setUrl(prop.getProperty("url"));
		    config.setUsername(prop.getProperty("username"));
		    config.setPassword(prop.getProperty("password"));
		    String pool = (prop.getProperty("pool"));
		    if (pool != null) {
		    		config.setPool(pool);
		    }
		    String maxPoolSize = prop.getProperty("maxPoolSize");
		    if(maxPoolSize != null) {
		    	config.setMaxPoolSize(Integer.parseInt(maxPoolSize));
		    }
		    
		    String maxStatementSize = prop.getProperty("maxStatementSize");
		    if(maxStatementSize != null) {
		    	config.setMaxStatementSize(Integer.parseInt(maxStatementSize));
		    }
		    
		    String minPoolSize = prop.getProperty("minPoolSize");
		    if(minPoolSize != null) {
		    	config.setMinPoolSize(Integer.parseInt(minPoolSize));
		    }
		} 
		catch (Exception ex) {
			logger.error("Failed to load the config file!", ex);
			throw new DBException("Cannot read the config file: database.properties. Please make sure the file is present in classpath.", ex);
		}
		
		return config;
	}


	@Override
	public DataSource get() {
		return dataSource;
	}

}
