package io.spring.training.corespring.personalbudgettracker.input_types;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.testconfig.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.users.UserService;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserDetails;

public class InputTypeServiceTests {

    private UserService userService;
    private InputTypeService inputTypeService;

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext context = SpringApplication.run(TestInfrastructureConfig.class);
        inputTypeService = context.getBean(InputTypeService.class);
        userService = context.getBean(UserService.class);
    }

    /**
     * Test createInputType : This takes in an input type object (with user already)
     * */
    UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
    User user = userService.getUserByUserName("JohnDoe");
    // InputType creation is stupid, it should take in a userId or userName and a inputtypedetails right?
    // Also, should inputtype be responsible for looking after inputsubtype or should that just be handled by the repo?

    // TODO: Test update input type

    // TODO: test delete input type

    // TODO: test createinputsubtype

    // TODO: test updatedInputSubtype

    // TODO: test deleteInputSubType

    // TODO: test find all input types by user id

    // TODO: test find all input subtypes by user id
}
