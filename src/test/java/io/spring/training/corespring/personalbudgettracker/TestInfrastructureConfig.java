package io.spring.training.corespring.personalbudgettracker;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.spring.training.corespring.personalbudgettracker.config.AspectsConfig;
import io.spring.training.corespring.personalbudgettracker.config.DataConfig;
import io.spring.training.corespring.personalbudgettracker.config.ServiceConfig;

@Configuration
@Import({TestInfrastructureLocalConfig.class, ServiceConfig.class, DataConfig.class, AspectsConfig.class})
public class TestInfrastructureConfig {

}
