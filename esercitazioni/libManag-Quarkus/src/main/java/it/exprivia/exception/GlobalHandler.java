package it.exprivia.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalHandler implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException ex) {
        return Response.status(ex.getStatusCode())
                .entity(ex.getMessage())
                .build();
    }



}
