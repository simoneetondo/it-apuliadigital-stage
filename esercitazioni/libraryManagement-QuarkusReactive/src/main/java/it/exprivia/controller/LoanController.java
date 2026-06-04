package it.exprivia.controller;

import io.smallrye.mutiny.Uni;
import it.exprivia.service.ILoan;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/loans")
@Tag(name = " Loans ", description = " Operations related to loans")
public class LoanController {

    @Inject
    ILoan service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getLoans() {
        return service.getAllLoans(0, 10)
                .map(loans -> Response.ok(loans).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{codiceFiscale}")
    public Uni<Response> getLoansByUser(@PathParam("codiceFiscale") String codiceFiscale) {
        return service.getLoansByUser(codiceFiscale)
                .map(loans -> Response.ok(loans).build());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{codiceFiscale}/{isbn}")
    public Uni<Response> createLoan(@QueryParam("codiceFiscale") String codiceFiscale, @QueryParam("isbn") String isbn) {
        return service.createLoan(codiceFiscale, isbn)
                .map(loan -> Response.status(Response.Status.CREATED)
                        .entity(loan)
                        .build());

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/return/{codiceFiscale}/{isbn}")
    public Uni<Response> returnLoan(@PathParam("codiceFiscale") String codiceFiscale, @PathParam("isbn") String isbn) {
        return service.returnLoan(codiceFiscale, isbn)
                .map(loan -> Response.ok(loan).build());
    }
}
