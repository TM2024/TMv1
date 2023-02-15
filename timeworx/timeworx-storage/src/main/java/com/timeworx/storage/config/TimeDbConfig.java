package com.timeworx.storage.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import java.beans.PropertyVetoException;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/15 8:50 PM
 */
@Component
@MapperScan(
        basePackages = {"com.timeworx.storage.mapper.*"},
        sqlSessionFactoryRef = "timeSqlSessionFactory"
)
public class TimeDbConfig {
    @Value("${jdbc.time.driverClass}")
    private String driverClass;
    @Value("${jdbc.time.jdbcUrl}")
    private String jdbcUrl;
    @Value("${jdbc.time.user}")
    private String user;
    @Value("${jdbc.time.password}")
    private String password;
    @Value("${jdbc.initialPoolSize}")
    private int initialPoolSize;
    @Value("${jdbc.maxIdleTime}")
    private int maxIdleTime;
    @Value("${jdbc.maxPoolSize}")
    private int maxPoolSize;
    @Value("${jdbc.minPoolSize}")
    private int minPoolSize;
    @Value("${jdbc.checkoutTimeout}")
    private int checkoutTimeout;
    @Value("${jdbc.acquireIncrement}")
    private int acquireIncrement;
    @Value("${jdbc.acquireRetryAttempts}")
    private int acquireRetryAttempts;
    @Value("${jdbc.acquireRetryDelay}")
    private int acquireRetryDelay;
    @Value("${jdbc.idleConnectionTestPeriod}")
    private int idleConnectionTestPeriod;

    @Bean(name = "timeDataSource")
    public ComboPooledDataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setInitialPoolSize(initialPoolSize);
        dataSource.setMaxIdleTime(maxIdleTime);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setCheckoutTimeout(checkoutTimeout);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
        dataSource.setAcquireRetryDelay(acquireRetryDelay);
        dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        return dataSource;
    }

    @Bean(name = "timeSqlSessionFactory")
    public SqlSessionFactory getSessionFactory(@Qualifier("timeDataSource") ComboPooledDataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

    @Bean(name = "timeTrans")
    public DataSourceTransactionManager getTransactionManager(@Qualifier("timeDataSource") ComboPooledDataSource dataSource){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
