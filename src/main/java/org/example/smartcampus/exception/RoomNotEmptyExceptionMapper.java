package org.example.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException e) {
        ErrorMessage error = new ErrorMessage(409, e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(error).build();
    }
}
