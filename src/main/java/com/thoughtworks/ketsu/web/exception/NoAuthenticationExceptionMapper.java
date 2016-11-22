package com.thoughtworks.ketsu.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoAuthenticationExceptionMapper implements ExceptionMapper<NoAuthenticationException> {
    @Override
    public Response toResponse(NoAuthenticationException exception) {
        return Response.status(401).build();
    }
}
