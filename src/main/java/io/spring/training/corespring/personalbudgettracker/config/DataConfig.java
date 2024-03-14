package io.spring.training.corespring.personalbudgettracker.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.JdbcInputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.JdbcInputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.inputs.internal.input.InputRepository;
import io.spring.training.corespring.personalbudgettracker.inputs.internal.input.JdbcInputRepository;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.JdbcUserRepository;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserRepository;
/**
 * Provides datasource configuration for all jdbc
 */
@Configuration
public class DataConfig {

    private DataSource dataSource;
    
    // AS this is the only constructor, @Autowired is not required
    public DataConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean 
    public UserRepository userRepository() {
        JdbcUserRepository repo = new JdbcUserRepository();
        repo.setDataSource(this.dataSource);
        return repo;
    }

    @Bean 
    public InputRepository inputRepository() {
        JdbcInputRepository repo = new JdbcInputRepository();
        repo.setDataSource(this.dataSource);
        return repo;
    }

    @Bean 
    public InputTypeRepository inputTypeRepository() {
        JdbcInputTypeRepository repo = new JdbcInputTypeRepository();
        repo.setDataSource(this.dataSource);
        return repo;
    }

    @Bean 
    public InputSubTypeRepository inputSubTypeRepository() {
        JdbcInputSubTypeRepository repo = new JdbcInputSubTypeRepository();
        repo.setDataSource(this.dataSource);
        return repo;
    }

    
}
