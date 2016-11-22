package com.thoughtworks.ketsu.web.jersey;

import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriInfo;

public class RoutesFactory implements Factory<Routes> {
    private Provider<UriInfo> uriInfoProvider;

    @Inject
    public RoutesFactory(Provider<UriInfo> uriInfoProvider) {
        this.uriInfoProvider = uriInfoProvider;
    }

    @Override
    public Routes provide() {
        return new Routes(uriInfoProvider.get());
    }

    @Override
    public void dispose(Routes instance) {
    }
}
