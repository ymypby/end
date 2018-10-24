package com.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by ymy on 2018/7/12.
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    @Value("${datasource.driver-class-name}")
    private String DRIVER;

    @Value("${datasource.password}")
    private String PASSWORD;

    @Value("${datasource.url}")
    private String URL;

    @Value("${datasource.username}")
    private String USERNAME;

    @Value("${jpa.properties.hibernate.dialect}")
    private String DIALECT;

    @Value("${jpa.properties.hibernate.show_sql}")
    private String SHOW_SQL;

    @Value("${jpa.properties.hibernate.hbm2ddl.auto}")
    private String HBM2DDL_AUTO;

    @Value("${jpa.properties.entitymanager.packagesToScan}")
    private String PACKAGES_TO_SCAN;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", DIALECT);
        hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
        hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;

    }
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(100);
        return dataSource;

    }


//    @Bean(name = "entityManagerFactory")
//    public EntityManagerFactory entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        return emf.getObject();
//    }




    }
