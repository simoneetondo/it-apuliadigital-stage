package it.gs;

import it.models.TvSerie;
import it.proxy.TvSeriesProxy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class TvSeriesService {

    @RestClient
    @Inject
    TvSeriesProxy seriesProxy;

    @Timeout(1000)                // 1. NON ASPETTARE: Se dopo 1 secondo non risponde, interrompi.
    @Retry(maxRetries = 2)        // 2. RIPROVA: Se fallisce (o va in timeout), riprova al massimo 2 volte.
    @CircuitBreaker(              // 3. PROTEGGI: Se su 10 chiamate, 5 falliscono, apri l'interruttore.
            requestVolumeThreshold = 10,
            failureRatio = 0.5,
            delay = 15000             // Tieni l'interruttore aperto per 15 secondi.
    )
    @Fallback(fallbackMethod = "getFallbackDetails") // 4. PIANO B: Se tutto sopra fallisce, usa questo.
    public TvSerie get(String title) {
        return seriesProxy.get(title);

    }
    public TvSerie getFallbackDetails(String title) {
        TvSerie fallback = new TvSerie();
        fallback.setName("Titolo non disponibile");
        fallback.setSummary("Non siamo riusciti a recuperare i dettagli della serie TV. Riprova più tardi.");
        return fallback;
    }

}
