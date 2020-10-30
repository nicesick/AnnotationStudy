package com.study.annotationStudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource routeDataSource() {
        DataSource routeDataSource = new RoutingDataSourceConfig(dataSourceMysql(), dataSourceH2());
        return new LazyConnectionDataSourceProxy(routeDataSource);
    }

    public DataSource dataSourceMysql() {
        final String PREFIX = "spring.datasource.mysql.";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty(PREFIX + "driver-class-name"));
        dataSource.setUrl(environment.getProperty(PREFIX + "url"));
        dataSource.setUsername(environment.getProperty(PREFIX + "username"));
        dataSource.setPassword(environment.getProperty(PREFIX + "password"));

        return dataSource;
    }

    public DataSource dataSourceH2() {
        final String PREFIX = "spring.datasource.h2.";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty(PREFIX + "driver-class-name"));
        dataSource.setUrl(environment.getProperty(PREFIX + "url"));
        dataSource.setUsername(environment.getProperty(PREFIX + "username"));
        dataSource.setPassword(environment.getProperty(PREFIX + "password"));

        return dataSource;
    }
}
