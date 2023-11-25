package org.hliu.guice.jdbc;

import jakarta.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionalMethodInterceptor implements MethodInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(TransactionalMethodInterceptor.class);

    @Inject
    private JdbcTransactionManager transactionManager;

    @Override
    public Object invoke(MethodInvocation method) throws Throwable {

        try {
            // Start the transaction
            transactionManager.begin();

            Object result = method.proceed();
            transactionManager.commit();
            return result;
        } catch (Exception e) {
            // check if the annotation NoRollback is available, if not, rollback
            logger.debug("The exception class: {}.", e.getClass());
            if (e.getClass().isAnnotationPresent(NoRollback.class)) {
                transactionManager.commit();
                throw e;
            }

            logger.info("Failed to commit transaction!", e);
            try {
                transactionManager.rollback();
            } catch (Exception ex) {
                logger.warn("Cannot roll back transaction!", ex);
            }
            throw e;
        }
    }

}
