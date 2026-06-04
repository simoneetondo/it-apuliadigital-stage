package it.exprivia.controller;

import io.smallrye.mutiny.Uni;
import it.exprivia.models.dtos.BookDTO;
import it.exprivia.models.entity.Book;
import it.exprivia.services.impl.IBook;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.print.attribute.standard.Media;
import java.util.List;

@Path("/api/books")
@Tag(name = "Books", description = "Operations related to books")
public class BookController {

    @Inject
    IBook service;

    @APIResponse(
            responseCode = "200",
            description = "List of books retrieved successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            type = SchemaType.ARRAY,
                            implementation = Book.class
                    )
            )
    )
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {

        return Response.ok(service.getBooks(0, 10)).build();
    }


    @GET
    @Path("/search/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksByTitle(@PathParam("title") String title) {
        return Response.ok(service.getBooksByTitle(0, 10, title)).build();

    }

    @Operation(
            summary = "Get a book by ISBN",
            description = "Returns a single book that matches the provided ISBN. If no book is found, a 404 Not Found response is returned."
    )
    @APIResponse(
            responseCode = "200",
            description = "Book found and returned successfully",
            content = @Content(mediaType = "application/json")
    )
    @APIResponse(
            responseCode = "404",
            description = "No book found with the provided ISBN",
            content = @Content(mediaType = "application/json")
    )
    @GET
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getBookByIsbn(@PathParam("isbn") String isbn) {
        return service.getBookByIsbnAsynch(isbn)
                .onItem().transform(b-> Response.ok(b).build());

//        return Response.ok(service.getBookByIsbnAsynch(isbn)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(@RequestBody BookDTO book) {
        BookDTO created = service.createBook(book);
        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();

    }

    @PUT
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("isbn") String isbn, @RequestBody BookDTO dto) {
        BookDTO updated = service.updateBook(isbn, dto);
        return Response.status(Response.Status.OK)
                .entity(updated)
                .build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response deleteBook(@PathParam("isbn") String isbn) {
        service.deleteBook(isbn);
        return Response.status(Response.Status.NO_CONTENT)
                .build();

    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> test() {
       return service.test();
    }

    @GET
    @Path("/test/{nome}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> test1(@PathParam("nome") String nome) {
        return service.test1(nome);
    }

}

