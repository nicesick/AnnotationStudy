package com.study.annotationStudy.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class RoutingDataSourceConfig extends AbstractRoutingDataSource {
    public RoutingDataSourceConfig(DataSource dataSourceMysql, DataSource dataSourceH2) {
        Map<Object, Object> dataSourceMap = new HashMap<>();

        dataSourceMap.put("master"  , dataSourceMysql);
        dataSourceMap.put("slave"   , dataSourceH2);

        super.setDefaultTargetDataSource(dataSourceMysql);
        super.setTargetDataSources(dataSourceMap);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("transactionActive : " + transactionActive);

        if (transactionActive) {
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            System.out.println("readOnly : " + readOnly);

            if (readOnly) {
                return "slave";
            }
        }

        return "master";
    }
}
