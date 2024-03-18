package io.spring.training.corespring.personalbudgettracker;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import io.spring.training.corespring.personalbudgettracker.config.AspectsConfig;
import io.spring.training.corespring.personalbudgettracker.config.ServiceConfig;

@Configuration
@Import({ServiceConfig.class, AspectsConfig.class})
public class TestInfrastructureConfig {
    @Bean
    public DataSource dataSource() {
        return (new EmbeddedDatabaseBuilder())
                .addScript("classpath:testdb/schema.sql")
                .addScript("classpath:testdb/data.sql")
                .build();
    }
}
