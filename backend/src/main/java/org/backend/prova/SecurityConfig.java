package org.backend.prova; // GARANTA que este é o pacote correto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita CSRF
                .csrf(csrf -> csrf.disable())

                // 2. Desabilita CORS do Security temporariamente (o do WebMvc já cuida disso)
                .cors(cors -> cors.disable())

                .authorizeHttpRequests(auth -> auth
                        // 3. LIBERA ABSOLUTAMENTE TUDO. Sem exceção.
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
