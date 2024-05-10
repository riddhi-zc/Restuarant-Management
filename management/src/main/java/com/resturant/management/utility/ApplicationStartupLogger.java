package com.resturant.management.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class ApplicationStartupLogger implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupLogger.class);

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private int serverPort;

    private final Environment environment;

    public ApplicationStartupLogger(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("---------------------------------------------------------------");
        logger.info("Application {}", applicationName);
        logger.info("---------------------------------------------------------------");
        logger.info("Application is on running on port: {}",serverPort);
        logger.info("---------------------------------------------------------------");
        logger.info("Active profiles: {}", String.join(", ", environment.getActiveProfiles()));

    }
}
