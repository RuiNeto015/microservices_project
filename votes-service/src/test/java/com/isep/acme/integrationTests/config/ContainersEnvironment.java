package com.isep.acme.integrationTests.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public class ContainersEnvironment {
    private static final String targetDatabase;

    static GenericContainer<?> container;

    static {
        targetDatabase = PropertiesExtractor.getProperty("spring.repositories.targetPackage");

        if (targetDatabase.equals("postgresql")) {
            container = new PostgreSQLContainer<>("postgres:16.0")
                    .withDatabaseName("test").withUsername("admin").withPassword("admin")
                    .withInitScript("schema.sql");

            container.start();
        } else if (targetDatabase.equals("mongodb")) {
            container = new MongoDBContainer("mongo:7.0.2");
            container.start();
        } else if (targetDatabase.equals("neo4j")) {
            container = new Neo4jContainer<>("neo4j:5.12.0");
        }
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        if (targetDatabase.equals("postgresql")) {
            PostgreSQLContainer<?> postgreSQLContainer = (PostgreSQLContainer<?>) container;

            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        } else if (targetDatabase.equals("mongodb")) {
            MongoDBContainer mongoDBContainer = (MongoDBContainer) container;
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        } else if (targetDatabase.equals("neo4j")) {

        }
    }
}
