package config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import input_types.internal.input_subtype.InputSubTypeRepository;
import input_types.internal.input_subtype.JdbcInputSubTypeRepository;
import input_types.internal.input_type.InputTypeRepository;
import input_types.internal.input_type.JdbcInputTypeRepository;
import inputs.internal.input.InputRepository;
import inputs.internal.input.JdbcInputRepository;
import users.internal.user.JdbcUserRepository;
import users.internal.user.UserRepository;


public class DataConfigTests {
    // Provide a mock for testing
    private DataSource dataSource = Mockito.mock(DataSource.class);

    private DataConfig dataConfig = new DataConfig(dataSource);

    @Test
    public void getBeans() {
        UserRepository userRepository = dataConfig.userRepository();
        assertTrue(userRepository instanceof JdbcUserRepository);
        checkDataSource(userRepository);

        InputRepository inputRepository = dataConfig.inputRepository();
        assertTrue(inputRepository instanceof JdbcInputRepository);
        checkDataSource(inputRepository);

        InputTypeRepository inputTypeRepository = dataConfig.inputTypeRepository();
        assertTrue(inputTypeRepository instanceof JdbcInputTypeRepository);
        checkDataSource(inputTypeRepository);

        InputSubTypeRepository inputSubTypeRepository = dataConfig.inputSubTypeRepository();
        assertTrue(inputSubTypeRepository instanceof JdbcInputSubTypeRepository);
        checkDataSource(inputSubTypeRepository);
        
    }

    private void checkDataSource(Object repository) {
        Class<? extends Object> repositoryClass = repository.getClass();

        try {
            Field dataSource = repositoryClass.getDeclaredField("dataSource");
            dataSource.setAccessible(true);
            assertNotNull(dataSource.get(repository));
        } catch (Exception e) {
			String failureMessage = "Unable to validate dataSource in " + repositoryClass.getSimpleName();
			System.out.println(failureMessage);
			e.printStackTrace();
			Fail.fail(failureMessage);
        }
    }
}
