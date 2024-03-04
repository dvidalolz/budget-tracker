package config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import input_types.internal.input_subtype.InputSubTypeRepository;
import input_types.internal.input_subtype.JdbcInputSubTypeRepository;
import input_types.internal.input_type.InputTypeRepository;
import input_types.internal.input_type.JdbcInputTypeRepository;
import inputs.internal.input.InputRepository;
import inputs.internal.input.JdbcInputRepository;
import users.internal.user.JdbcUserRepository;
import users.internal.user.UserRepository;

@Configuration
public class DataConfig {

    private DataSource dataSource;
    
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
