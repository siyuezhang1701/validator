package com.thoughtworks.ketsu.web.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
//        exception.printStackTrace();
        return Response.status(400).type(MediaType.APPLICATION_JSON).entity(new HashMap<String, Object>() {{
            put("message", exception.getMessage());
        }}).build();
    }
}
