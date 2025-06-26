package jdr.generator.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    // GESTION DES PORTS D'ÉCOUTE AUTORISÉS :
    List<String> baseUrls =
        Arrays.asList(
            "http://localhost", // L'adresse d'origine par défaut de l'application
            "http://127.0.0.1" // L'adresse IP loopback
            );
    List<Integer> ports =
        Arrays.asList(
            80, // Port par défaut HTTP / WEB
            5173, // Port de développement pour Vite/React/Angular
            3000, // Port par défaut de nouvelles IA
            3001, // GEMINI
            3002, // OPENAI
            3003, // FREEPIK
            3006, // MYSQL
            3007, // MYSQL (redirection/autre service)
            3080, // KUBERNETES (port utilisé via kubectl port-forward)
            9000 // JENKINS
            );
    List<String> allowedOrigins = new ArrayList<>();
    // Ajout des URLs de base sans port (nécessaire sur certaines configs spécifiques)
    allowedOrigins.addAll(baseUrls);
    // Génération des combinaisons base_url:port
    for (String baseUrl : baseUrls) {
      for (Integer port : ports) {
        allowedOrigins.add(String.format("%s:%d", baseUrl, port));
      }
    }
    config.setAllowedOrigins(allowedOrigins);

    config.setAllowedHeaders(
        Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
