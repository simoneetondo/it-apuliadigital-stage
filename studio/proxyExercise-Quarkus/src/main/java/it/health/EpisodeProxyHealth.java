package it.health;

import it.proxy.TvSeriesProxy;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Readiness
public class EpisodeProxyHealth implements HealthCheck {

    @Inject
    @RestClient
    TvSeriesProxy proxy;


    @Override
    public HealthCheckResponse call() {
        try {
        proxy.get("title");
        return HealthCheckResponse.named("Tv Episode").up().build(); }
        catch (Exception e) {
            return HealthCheckResponse.named("Tv Episode").down().build();
        }
    }

}
