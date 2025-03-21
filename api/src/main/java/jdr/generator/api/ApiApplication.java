package jdr.generator.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApiApplication {

    private static final Logger LOGGER = LogManager.getLogger();

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        logAppReady();
    }

    private static void logAppReady() {
        LOGGER.log(Level.OFF, "LOGGER LEVEL '" + LOGGER.getLevel() + "' WILL BE USED...");
        final String message = "\n---------\n\nJDR-GENERATOR : APP IS READY TO USE !\n\n---------";
        LOGGER.log(Level.OFF, message);
        System.out.println(message);
    }

}
