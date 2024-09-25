package com.mxpioframework.dbconsole.utils;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class SpringJdbcUtils {
	public static TransactionTemplate getTransactionTemplate(DataSource dataSource) {  
        PlatformTransactionManager txManager = new DataSourceTransactionManager(  
        		dataSource);
        return new TransactionTemplate(txManager);
    }  
  
    public static JdbcTemplate getJdbcTemplate(TransactionTemplate txTemplate) {  
        DataSourceTransactionManager txManager = (DataSourceTransactionManager) txTemplate  
                .getTransactionManager();  
        DataSource dataSource = txManager.getDataSource();
        return new JdbcTemplate(dataSource);
    }  
  
    public static JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }  
  

}
