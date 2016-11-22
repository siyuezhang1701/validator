package com.thoughtworks.ketsu;

import com.thoughtworks.ketsu.web.jersey.Api;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import java.net.URI;

import static org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory.createHttpServer;

public class MainServer {
    public static void main(String[] args) throws Exception {
        String contextPath = System.getenv().getOrDefault("CONTEXT_PATH", "/");
        WebappContext context = new WebappContext("Stacks", contextPath);
        context.setAttribute(ServletProperties.SERVICE_LOCATOR, Injections.createLocator());
        ServletRegistration servletRegistration = context.addServlet("ServletContainer",
                new ServletContainer(ResourceConfig.forApplicationClass(Api.class)));

        servletRegistration.addMapping("/*");

        HttpServer server = null;
        try {
            server = createHttpServer(URI.create("http://0.0.0.0:" + System.getenv().getOrDefault("SERVICE_PORT", "8088")));
            context.deploy(server);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error deploy");
            System.exit(1);
        }

        server.start();
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                server.shutdownNow();
            }
        }
    }
}
