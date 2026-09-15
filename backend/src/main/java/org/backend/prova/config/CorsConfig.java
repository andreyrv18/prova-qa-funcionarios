package org.backend.prova.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // Permite QUALQUER origem
                .allowedMethods("*")          // Permite TODOS os métodos HTTP
                .allowedHeaders("*")          // Permite TODOS os headers
                .allowCredentials(true)       // Permite cookies/autenticação
                .maxAge(3600);                // Cache por 1 hora
    }
}