package com.study.annotationStudy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource dataSourceMysql() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.h2")
    public DataSource dataSourceH2() {
        return DataSourceBuilder.create().build();
    }
}
