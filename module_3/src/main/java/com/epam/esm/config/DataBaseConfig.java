package com.epam.esm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DataBaseConfig {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(100);
        ds.setDriverClassName(environment.getRequiredProperty("spring.driverClassName"));
        ds.setJdbcUrl(environment.getRequiredProperty("spring.datasource.url"));
        ds.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        ds.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        return ds;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}
