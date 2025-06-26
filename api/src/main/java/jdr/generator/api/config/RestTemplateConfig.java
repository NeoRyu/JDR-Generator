package jdr.generator.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for RestTemplate.
 *
 * <p>This class configures a RestTemplate bean for making HTTP requests.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Configures and provides a RestTemplate instance.
     *
     * @return A RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
