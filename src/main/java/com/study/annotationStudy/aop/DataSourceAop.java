package com.study.annotationStudy.aop;

import com.study.annotationStudy.repository.HomeRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Aspect
@Component
public class DataSourceAop {
    private DataSource dataSource;

    @Autowired
    public DataSourceAop(@Qualifier("routeDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Before("@annotation(com.study.annotationStudy.annotation.DynamicDataSource)")
    public void matchDataSource(JoinPoint jp) {
        HomeRepository target = (HomeRepository) jp.getTarget();
        JdbcTemplate targetJdbcTemplate = target.getJdbcTemplate();

        targetJdbcTemplate.setDataSource(dataSource);
    }
}
