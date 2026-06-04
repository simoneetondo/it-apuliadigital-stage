package it.exprivia.Scuola.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// prende i dati direttamente dall'application properties, e serve al service
@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
@Data // getters and setters automatici
public class JwtConfig {
    private String secretKey;
    private long expiration;
}