package jdr.generator.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * JDR-GENERATOR Main Application Class.
 *
 * <p>This class serves as the entry point for the JDR-GENERATOR application. It is a Spring Boot
 * application that enables asynchronous task execution and scheduled tasks. It also configures
 * Cross-Origin Resource Sharing (CORS) to allow requests from any origin.
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
@CrossOrigin(origins = {"*"})
public class ApiApplication {

  private static final Logger log = LogManager.getLogger();

  /**
   * Main method to start the JDR-GENERATOR application.
   *
   * <p>This method starts the Spring Boot application and logs a message indicating that the
   * application is ready to use.
   *
   * @param args Command line arguments passed to the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
    logAppReady();
  }

  /** Logs a message indicating that the application is ready. */
  private static void logAppReady() {
    log.log(Level.OFF, "LOGGER LEVEL '{}' WILL BE USED...", log.getLevel());
    final String message = "\n---------\n\nJDR-GENERATOR : APP IS READY TO USE !\n\n---------";
    log.log(Level.OFF, message);
    if (log.isInfoEnabled()) {
      log.info(message);
    }
  }
}
