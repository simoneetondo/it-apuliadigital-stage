package it.exprivia;

import it.exprivia.models.dtos.BookDTO;
import it.exprivia.models.entity.Book;
import it.exprivia.services.impl.IBook;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;


public class GreetingResource {


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }


}

