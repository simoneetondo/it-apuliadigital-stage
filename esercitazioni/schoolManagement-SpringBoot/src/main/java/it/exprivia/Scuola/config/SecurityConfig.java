package it.exprivia.Scuola.config;

import it.exprivia.Scuola.security.JwtAuthenticatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticatorFilter jwtAuthenticatorFilter;

    @Value("${security.enabled:true}")
    private boolean securityEnabled;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        if (!securityEnabled) {
            return http
                    .csrf(csrf -> csrf.disable()) // Disabilita CSRF per testare le POST
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()) // Apre tutti gli endpoint (Student, Teacher, ecc.)
                    .anonymous(anonymous -> anonymous
                            .authorities("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT")
                    )
                    // Fondamentale per vedere la console di H2 nel browser
                    .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                    .build();
        }

        http
                .csrf(csrf -> csrf.disable())  // Disabilita CSRF per test
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll() // diciamo che ad accesso ed autenticazione non serve l'autenticazione

                        // permette l'accesso a swagger senza autenticazione
                        // in ambiente di sviluppo permette ai frontendisti di provare le API senza impazzire con i token
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // motivo per cui non compilava era perchè mancava questo alla fine della catena di authorities
                        .anyRequest().authenticated()  // Permetti tutte le richieste


                )
                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        // 1. STATELESS: FONDAMENTALE quando si lavora con i jwt, non crea mai HttpSession nel server e
                        // dice a spring di non utilizzare nemmeno i cookie
                        // 2. ogni richiesta è unica, il server tratterò ogni chiamata come se fosse la prima, l'unico modo
                        // per farsi riconoscere è tramite token

                )
                // DICIAMO A SPRING DI USARE IL NOSTRO FILTRO
                // lo inseriamo PRIMA del filtro di autenticazione standard di spring
                .addFilterBefore(jwtAuthenticatorFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}