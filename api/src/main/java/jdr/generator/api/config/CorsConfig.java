package jdr.generator.api.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS).
 *
 * <p>This class defines the CORS configuration for the application, allowing requests from
 * specified origins and with specific headers and methods.
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
                    "http://localhost", // Origine du front-end si sur le port 80 (par défaut)
                    "http://localhost:80", // Explicitement le port 80
                    "http://localhost:5173",
                    "http://localhost:4200",
                    "http://localhost:3000",
                    "http://localhost:3001",
                    "http://localhost:3002",
                    "http://localhost:3003",
                    "http://localhost:3006",
                    "http://localhost:3007",
                    "http://127.0.0.1",     // L'adresse IP loopback
                    "http://127.0.0.1:80",
                    "http://127.0.0.1:5173",
                    "http://127.0.0.1:4200",
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:3001",
                    "http://127.0.0.1:3002",
                    "http://127.0.0.1:3003",
                    "http://127.0.0.1:3006",
                    "http://127.0.0.1:3007"
            ));
    config.setAllowedHeaders(
        Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
