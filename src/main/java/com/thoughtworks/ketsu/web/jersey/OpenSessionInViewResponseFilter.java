package com.thoughtworks.ketsu.web.jersey;


import org.apache.ibatis.session.SqlSessionManager;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class OpenSessionInViewResponseFilter implements ContainerResponseFilter {
    @Inject
    private Provider<SqlSessionManager> sqlSessionManagerProvider;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (sqlSessionManagerProvider.get().isManagedSessionStarted()) {
            try {
                if (responseContext.getStatus() < Response.Status.BAD_REQUEST.getStatusCode()) {
                    sqlSessionManagerProvider.get().commit(true);
                } else {
                    sqlSessionManagerProvider.get().rollback(true);
                }
            } finally {
                sqlSessionManagerProvider.get().close();
            }
        }
    }
}
