package io.spring.training.corespring.personalbudgettracker;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.spring.training.corespring.personalbudgettracker.config.Config;


@Configuration
@Import({TestInfrastructureLocalConfig.class, Config.class})
public class TestInfrastructureConfig {

}
