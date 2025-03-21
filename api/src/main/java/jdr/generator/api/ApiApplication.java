package jdr.generator.api;

import io.micrometer.core.instrument.util.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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

    private static void logBanner() {
        try {
            final String banner = IOUtils.toString((InputStream) Objects.requireNonNull(ApiApplication.class.getResource("/banner.txt")).getContent(), StandardCharsets.UTF_8);
            LOGGER.log(Level.OFF, banner);
        } catch (IOException e) {
            LOGGER.log(Level.OFF, "FAILED TO READ BANNER");
        }
    }

    private static void logAppReady() {
        logBanner();
        LOGGER.log(Level.OFF, "LOGGER LEVEL '" + LOGGER.getLevel() + "' WILL BE USED...");
        final String message = "\n---------\n\nJDR-GENERATOR : APP IS READY TO USE !\n\n---------";
        LOGGER.log(Level.OFF, message);
        System.out.println(message);
    }

}
