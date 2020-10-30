# AnnotationStudy
This is a project for making annotation for connecting with database dynamically



## Environment

* Spring-Boot 2.3.4.RELEASE
* MySql       8.0.21 for Win64 on x86_64
* H2          1.4.200



## Study Key Point

* Annotation, AOP
* AbstractRoutingDataSource



### 1. Annotation, AOP

* How to make annotation
```java
    @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DynamicDataSource {
    }
```



* How to set AOP to catch annotation
>  you need to include aop in pom.xml
```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
```
> Add Annotation for aop in mainApplication
```java
    @SpringBootApplication
    @EnableAspectJAutoProxy
    public class AnnotationStudyApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(AnnotationStudyApplication.class, args);
        }
    }
```
> Set AOP to catch annotation
```java
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
```



### 2. AbstractRoutingDataSource
* How to make RoutingDataSource
> Set Properties for dataSource in application.properties
```properties
    spring.datasource.mysql.driver-class-name=com.mysql.cj.jdbc.Driver 
    spring.datasource.mysql.url=jdbc:mysql://localhost:3306/annotationStudy?serverTimezone=UTC&characterEncoding=UTF-8
    spring.datasource.mysql.username=annotationStudy
    spring.datasource.mysql.password=annotationStudy
    
    spring.datasource.h2.driver-class-name=org.h2.Driver
    spring.datasource.h2.url=jdbc:h2:tcp://localhost/~/annotationStudy
    spring.datasource.h2.username=sa
    spring.datasource.h2.password=
```



> Build each dataSource in configuration using properties
> In here, KeyPoint is LazyConnectionDataSourceProxy
> without this class, @Transactional annotation won't be executed
> 
> I found a Reference about this issue : [Reference - 이글루 : 까먹지말자!](http://kwon37xi.egloos.com/m/5364167)
```java
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
```



> Set routing dataSources and routing roles using AbstractRoutingDataSource class
```java
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
```



> Set Transaction in Service
> In my Case, routing role is ReadOnly Options
```java
    @Service
    @Transactional
    public class HomeService {
        private HomeRepository homeRepository;
    
        @Autowired
        public HomeService(HomeRepository homeRepository) {
            this.homeRepository = homeRepository;
        }
    
        @Transactional
        public List<Guest> getGuests() {
            return homeRepository.findAll();
        }
    
        @Transactional(readOnly = true)
        public List<Guest> getGuestsReadOnly() {
            return homeRepository.findAll();
        }
    }
```