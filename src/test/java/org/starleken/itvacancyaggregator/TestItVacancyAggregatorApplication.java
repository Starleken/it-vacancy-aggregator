package org.starleken.itvacancyaggregator;

import org.springframework.boot.SpringApplication;

public class TestItVacancyAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.from(ItVacancyAggregatorApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
