package org.hliu.guice.jdbc;

import java.util.Properties;

import com.google.inject.Provider;

public class JdbcConfigProvider implements Provider<JdbcConfig>{

	private JdbcConfig config;
	
	@Override
	public JdbcConfig get() {
		if(config != null) {
			return this.config;
		}
		
		config = new JdbcConfig();
		Properties prop = new Properties();
		try {
		    //load a properties file from class path, inside static method
		    prop.load(JdbcConfig.class.getClassLoader().getResourceAsStream("/database.properties"));

		    //get the property value and print it out
		    config.setDriver(prop.getProperty("driver"));
		    config.setUrl(prop.getProperty("url"));
		    config.setUsername(prop.getProperty("username"));
		    config.setPassword(prop.getProperty("password"));
		    
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
			throw new DBException("Cannot read the config file: database.properties. Please make sure the file is present in classpath.", ex);
		}
		
		return config;
	}

}
