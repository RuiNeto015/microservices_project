package com.isep.acme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Configuration
public class TransactionManagerConfig {

    @Value("${spring.database.entityManager}")
    private String entityManagerClassString;

    @Value("${spring.database.transactionManager}")
    private String transactionManagerClassString;

    @Bean
    PlatformTransactionManager transactionManager(ApplicationContext context) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<?> factoryClass = Class.forName(this.entityManagerClassString);
        Object factoryBean = context.getBean(factoryClass);

        Class<?> transactionManagerClass = Class.forName(this.transactionManagerClassString);
        Constructor<?> constructor = transactionManagerClass.getDeclaredConstructor(factoryClass);
        return (PlatformTransactionManager) constructor.newInstance(factoryBean);
    }
}
