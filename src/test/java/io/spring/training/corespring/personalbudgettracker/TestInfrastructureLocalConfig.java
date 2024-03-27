package io.spring.training.corespring.personalbudgettracker;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile("local")
public class TestInfrastructureLocalConfig {

    @Bean
    public DataSource dataSource() {
        return (new EmbeddedDatabaseBuilder())
                .addScript("classpath:testdb/schema.sql")
                .addScript("classpath:testdb/data.sql")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
