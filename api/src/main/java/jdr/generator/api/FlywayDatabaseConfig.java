package jdr.generator.api;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Flyway database migrations.
 * */
@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayDatabaseConfig {

  /**
   * Default configuration instance for Flyway database migrations.
   *
   * @param flywayProperties Configuration des propriétés de Flyway
   * @return Flyway
   */
  @Bean(initMethod = "migrate")
  public Flyway flyway(FlywayProperties flywayProperties) {
    return Flyway.configure()
        .dataSource(
            flywayProperties.getUrl(), flywayProperties.getUser(), flywayProperties.getPassword())
        .locations(flywayProperties.getLocations().toArray(String[]::new))
        .baselineOnMigrate(true)
        .load();
  }
}
