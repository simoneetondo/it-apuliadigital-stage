package it.health;


import it.proxy.TvSeriesProxy;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Readiness
public class TvSeriesProxyHealth implements HealthCheck {


    @Inject
    @RestClient
    TvSeriesProxy proxy;


    @Override
    public HealthCheckResponse call() {
        try {
            proxy.get("title");
            return HealthCheckResponse.named("tV")
                    .up()
                    .withData("version", "1.2.3-RELEASE")
                    .withData("environment", "production")
                    .withData("uptime", String.valueOf(LocalDateTime.now()))
                    .build();
        } catch (Exception e) {
            return HealthCheckResponse.named("Tvmaze APIs")
                    .down()
                    .withData("reason", "API non raggiungibile o errore di timeout")
                    .withData("exception_message", e.getMessage())
                    .build();
        }
    }
}
