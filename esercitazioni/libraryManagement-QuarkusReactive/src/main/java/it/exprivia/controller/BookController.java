package it.exprivia.controller;

import io.smallrye.mutiny.Uni;
import it.exprivia.models.dtos.book.BookCreateRequest;
import it.exprivia.models.dtos.book.BookDTO;
import it.exprivia.models.dtos.book.BookStockUpdateRequest;
import it.exprivia.service.IBook;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@Tag(name = " Books ", description = " Operations related to books")
@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookController {

    @Inject
    IBook service;


    @APIResponse(responseCode = "200", description = "List of books retrieved successfully")
    @GET
    @Path("")
    public Uni<Response> getBooks() {
        return service.getBooks(0, 10)
                .map(b -> Response.ok(b).build());
    }

    @APIResponse(responseCode = "200", description = "Books retrieved successfully by author")
    @GET
    @Path("/search/{author}")
    public Uni<Response> searchBooksByAuthor(@PathParam("author") String author) {
        return service.searchBooksByAuthor(author, 0, 10)
                .map(b -> Response.ok(b).build());

    }

    @APIResponse(responseCode = "200", description = "Books retrieved successfully by title")
    @GET
    @Path("/search/{title}")
    public Uni<Response> searchBookByTitle(@PathParam("title") String title) {
        return service.searchBooksByTitle(title, 0, 10)
                .map(b -> Response.ok(b).build());

    }

    @APIResponse(responseCode = "200", description = "Book retrieved successfully by ISBN")
    @GET
    @Path("/{isbn}")
    public Uni<Response> getBookByIsbn(@PathParam("isbn") String isbn) {
        return service.getBookByIsbn(isbn)
                .map(b -> Response.ok(b).build());
    }


    @APIResponse(responseCode = "201", description = "Book created succesfully")
    @APIResponse(responseCode = "400", description = "Invalid book data provided")
    @APIResponse(responseCode = "409", description = "Book with the same ISBN already exists")
    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createBook(@Valid BookCreateRequest bookDTO) {
        return service.createBook(bookDTO)
                .map(b -> Response.status(Response.Status.CREATED)
                        .entity(b)
                        .build());
    }

    @APIResponse(responseCode = "200", description = "Book updated successfully")
    @APIResponse(responseCode = "400", description = "Invalid book data provided")
    @APIResponse(responseCode = "404", description = "Book not found")
    @PUT
    @Path("/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> updateBook(@PathParam("isbn") String isbn, @Valid BookDTO bookDTO) {
        return service.updateBook(isbn, bookDTO)
                .map(b -> Response.ok(bookDTO).build());
    }

    @APIResponse(responseCode = "204", description = "Book deleted successfully")
    @APIResponse(responseCode = "404", description = "Book not found")
    @DELETE
    @Path("/{isbn}")
    public Uni<Response> deleteBook(@PathParam("isbn") String isbn) {
        return service.deleteBook(isbn)
                .map(b -> Response.status(Response.Status.NO_CONTENT)
                        .build());
    }

    @APIResponse(responseCode = "200", description = "Stock added successfully")
    @APIResponse(responseCode = "400", description = "Invalid stock update data provided")
    @APIResponse(responseCode = "404", description = "Book not found")
    @PATCH
    @Path("/stock/add/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addStock(@PathParam("isbn") String isbn, @Valid BookStockUpdateRequest request) {
        return service.addStock(isbn, request)
                .map(book -> Response.ok(book).build());

    }

    @APIResponse(responseCode = "200", description = "Stock removed successfully")
    @APIResponse(responseCode = "400", description = "Invalid stock update request")
    @APIResponse(responseCode = "404", description = "Book not found")
    @PATCH
    @Path("/stock/remove/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> removeStock(@PathParam("isbn") String isbn, @Valid BookStockUpdateRequest request) {
        return service.removeStock(isbn, request)
                .map(book -> Response.status(Response.Status.OK)
                        .entity(book)
                        .build());

    }


}
