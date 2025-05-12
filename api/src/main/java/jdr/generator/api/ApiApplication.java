package jdr.generator.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;


@EnableScheduling
@EnableAsync
@SpringBootApplication
@CrossOrigin(origins = { "*" })
public class ApiApplication {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        logAppReady();
    }

    private static void logAppReady() {
        LOGGER.log(Level.OFF, "LOGGER LEVEL '{}' WILL BE USED...", LOGGER.getLevel());
        final String message = "\n---------\n\nJDR-GENERATOR : APP IS READY TO USE !\n\n---------";
        LOGGER.log(Level.OFF, message);
        System.out.println(message);
    }

}
