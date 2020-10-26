package com.study.annotationStudy.aop;

import com.study.annotationStudy.repository.HomeRepository;
import com.study.annotationStudy.repository.JdbcTemplateHomeRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

@Aspect
@Component
public class DataSourceAop {
    private DataSource dataSourceMysql;
    private DataSource dataSourceH2;

    @Autowired
    public DataSourceAop(@Qualifier("dataSourceMysql") DataSource dataSourceMysql, @Qualifier("dataSourceH2") DataSource dataSourceH2) {
        this.dataSourceMysql    = dataSourceMysql;
        this.dataSourceH2       = dataSourceH2;
    }

    @Before("@annotation(com.study.annotationStudy.annotation.DynamicDataSource)")
    public void matchDataSource(JoinPoint jp) {
        HomeRepository target = (HomeRepository) jp.getTarget();
        JdbcTemplate targetJdbcTemplate = target.getJdbcTemplate();

        targetJdbcTemplate.setDataSource(this.dataSourceH2);
        targetJdbcTemplate.setDataSource(this.dataSourceMysql);

        System.out.println("matchDataSource " + this.dataSourceMysql);
        System.out.println("matchDataSource " + this.dataSourceH2);
        System.out.println("matchDataSource " + targetJdbcTemplate.toString());
    }
}
