package com.thoughtworks.ketsu.web.jersey;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class CORSResponseFilter
        implements ContainerResponseFilter {

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        final List<String> origins = asList(System.getenv().getOrDefault("ORIGINS", "http://localhost:8180").split(","));
        final String origin = requestContext.getHeaderString("Origin");
        if (origins.contains(origin)) {
            headers.add("Access-Control-Allow-Origin", origin);
        }
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia, Location");
        headers.add("Access-Control-Allow-Credentials", "true");
    }
}
