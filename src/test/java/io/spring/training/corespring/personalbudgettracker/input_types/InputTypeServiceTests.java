package io.spring.training.corespring.personalbudgettracker.input_types;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.testconfig.TestInfrastructureConfig;

public class InputTypeServiceTests {

    private InputTypeRepository inputTypeRepository;
    private InputSubTypeRepository inputSubTypeRepository;

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext context = SpringApplication.run(TestInfrastructureConfig.class);
        inputTypeRepository = context.getBean(InputTypeRepository.class);
        inputSubTypeRepository = context.getBean(InputSubTypeRepository.class);   
    }

    // TODO: Test createInputType

    // TODO: Test update input type

    // TODO: test delete input type

    // TODO: test createinputsubtype

    // TODO: test updatedInputSubtype

    // TODO: test deleteInputSubType

    // TODO: test find all input types by user id

    // TODO: test find all input subtypes by user id
}
