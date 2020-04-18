package org.hliu.guice.jdbc;


import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.Transactional;

public class GuiceJdbcModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(DataSource.class).toProvider(DataSourceProvider.class).in(Scopes.SINGLETON);
		bind(Session.class).toProvider(SessionProvider.class).in(Scopes.SINGLETON);
		
		bind(TransactionManager.class).to(JdbcTransactionManager.class);
		TransactionalMethodInterceptor transactionalMethodInterceptor = new TransactionalMethodInterceptor();
		requestInjection(transactionalMethodInterceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionalMethodInterceptor);
		
	}

}
