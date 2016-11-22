package com.thoughtworks.ketsu.web.jersey;


import org.apache.ibatis.session.SqlSessionManager;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;

@PreMatching
public class OpenSessionInViewRequestFilter implements ContainerRequestFilter {
    @Inject
    private Provider<SqlSessionManager> sqlSessionManagerProvider;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!sqlSessionManagerProvider.get().isManagedSessionStarted()) {
            sqlSessionManagerProvider.get().startManagedSession();
        }
    }
}
