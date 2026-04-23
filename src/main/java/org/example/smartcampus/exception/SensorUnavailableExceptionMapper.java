package org.example.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException e) {
        ErrorMessage error = new ErrorMessage(403, e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(error).build();
    }
}
