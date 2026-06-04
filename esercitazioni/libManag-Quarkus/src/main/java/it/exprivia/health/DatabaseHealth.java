package it.exprivia.health;

import it.exprivia.services.impl.BookServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;


@Readiness
public class DatabaseHealth implements HealthCheck {

    @Inject
    BookServiceImpl bookService;

    @Override
    @Transactional
    public HealthCheckResponse call() {
        try {
            bookService.checkDb();
            return HealthCheckResponse.up("Database is up and running");
        } catch (Exception e) {
            return HealthCheckResponse.down("Database is down: " + e.getMessage());
        }


    }
}
