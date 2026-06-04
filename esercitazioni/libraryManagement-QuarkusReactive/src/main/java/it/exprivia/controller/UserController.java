package it.exprivia.controller;

import io.smallrye.mutiny.Uni;
import it.exprivia.models.dtos.user.UserDTO;
import it.exprivia.service.IUser;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = " Users ", description = " Operations related to users")
@Path("/api/users")
public class UserController {

    @Inject
    IUser service;


    @APIResponse(responseCode = "200", description = "List of users retrieved successfully")
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUsers() {
        return service.getAllUsers(0, 10)
                .map(response -> Response.ok(response).build());
    }

    @APIResponse(responseCode = "200", description = "User retrieved successfully by codice fiscale")
    @APIResponse(responseCode = "404", description = "User not found with the provided codice fiscale")
    @GET
    @Path("/{codiceFiscale}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUsers(@PathParam("codiceFiscale") String codiceFiscale) {
        return service.getUserById(codiceFiscale)
                .map(user -> Response.ok(user).build());
    }

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(@Valid UserDTO newUser) {
        return service.createUser(newUser)
                .map(user -> Response.status(Response.Status.CREATED)
                        .entity(user)
                        .build());
    }

}
