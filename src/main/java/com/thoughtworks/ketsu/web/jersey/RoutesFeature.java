package com.thoughtworks.ketsu.web.jersey;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class RoutesFeature implements Feature {
    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(RoutesFactory.class, RequestScoped.class).to(Routes.class).in(RequestScoped.class);
            }
        });
        return true;
    }
}
