package jdr.generator.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS).
 *
 * <p>This class defines the CORS configuration for the application, allowing requests from specified
 * origins and with specific headers and methods.
 */
@Configuration
public class CorsConfig {

  /**
   * Configures and registers a {@link CorsFilter} to handle CORS requests.
   *
   * @return A {@link CorsFilter} instance with the defined CORS configuration.
   */
  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(
            Arrays.asList(
                    "http://localhost",
                    "http://localhost:5173")); // Add all necessary origins
    config.setAllowedHeaders(
            Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
