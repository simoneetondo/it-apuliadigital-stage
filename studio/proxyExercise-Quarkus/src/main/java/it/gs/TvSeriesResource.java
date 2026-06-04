package it.gs;

import it.models.TvSeriesFullDetails;
import it.proxy.EpisodeProxy;
import it.proxy.TvSeriesProxy;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import it.models.Episode;
import it.models.TvSerie;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/tvseries")
public class TvSeriesResource {

    @ConfigProperty(name = "dafault.title")
    String defaultTitle;

    @Inject
    TvSeriesService service;


    @Inject
    @RestClient
    EpisodeProxy episodeProxy;

    private final List<TvSerie> series = new ArrayList<>();

    @GET
    @Path("/fetch")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("title") String title) {
        if (title == null)
            title = defaultTitle;
        TvSerie tvSeries = service.get(title);

        List<Episode> episodes = episodeProxy.get(tvSeries.getId());

        TvSeriesFullDetails fullDetails = new TvSeriesFullDetails(tvSeries, episodes);
//        Map<String, Object> response = new HashMap<>();
//       response.put("serie", tvSeries);
//       response.put("episodes", episodes);
        return Response.ok(fullDetails).build();

    }

}
