package org.starleken.itvacancyaggregator.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final int POSTGRES_PORT = 5432;

    private static final String POSTGRES_DB = "vacancy_aggregator";
    private static final String POSTGRES_SCHEMA = "vacancy_aggregator_schema";
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "1111";

    private static final GenericContainer<?> POSTGRES =
            new GenericContainer<>("postgres:18.1-alpine")
                    .withEnv("POSTGRES_DB", POSTGRES_DB)
                    .withEnv("POSTGRES_USER", POSTGRES_USER)
                    .withEnv("POSTGRES_PASSWORD", POSTGRES_PASSWORD)
                    .waitingFor(Wait.forListeningPort())
                    .withExposedPorts(POSTGRES_PORT);

    static {
        POSTGRES.start();
    }

    private static String getHost() {
        return POSTGRES.getHost();
    }

    private static int getPort() {
        return POSTGRES.getMappedPort(POSTGRES_PORT);
    }

    private static String getUrl() {
        return "jdbc:postgresql:" + UriComponentsBuilder.newInstance()
                .host(getHost()).port(getPort()).path(POSTGRES_DB)
                .toUriString();
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        applyProperties(applicationContext);
    }

    private void applyProperties(final ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url:" + getUrl() + "?currentSchema=" + POSTGRES_SCHEMA,
                "spring.datasource.username:" + POSTGRES_USER,
                "spring.datasource.password:" + POSTGRES_PASSWORD,
                "spring.liquibase.url:" + getUrl(),
                "spring.liquibase.user:" + POSTGRES_USER,
                "spring.liquibase.password:" + POSTGRES_PASSWORD,
                "spring.liquibase.liquibase-schema:" + POSTGRES_SCHEMA
        ).applyTo(applicationContext);
    }
}

