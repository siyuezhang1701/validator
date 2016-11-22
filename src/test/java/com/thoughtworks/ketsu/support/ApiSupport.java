package com.thoughtworks.ketsu.support;

import com.thoughtworks.ketsu.util.Json;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ApiSupport {
    JerseyTest test;

    @Inject
    Application application;

    @Inject
    ClientConfigurator clientConfigurator;

    @Inject
    SetUp setUp;

    @Inject
    @Named("server_uri")
    private String serverUri;
    protected String token = "";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        test = new JerseyTest() {
            @Override
            protected Application configure() {
                return application;
            }

            @Override
            protected URI getBaseUri() {
                return URI.create(serverUri);
            }

            @Override
            protected void configureClient(ClientConfig config) {
                super.configureClient(config);
                clientConfigurator.config(config);
            }

            @Override
            protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
                return new TestContainerFactory((ResourceConfig) application);
            }
        };

        test.setUp();
        setUp.before();
    }

    @After
    public void tearDown() throws Exception {
        test.tearDown();
    }

    protected Response get(Invocation.Builder builder) {
        return session(builder).get();
    }

    public interface ClientConfigurator {
        void config(ClientConfig config);
    }

    protected Response post(String uri, Entity json) {
        return session(test.target(uri).request()).post(json);
    }

    protected Response post(String uri, Map<String, Object> json) {
        return session(test.target(uri).request()).post(Entity.json(json));
    }

    protected Response put(String uri, Map<String, Object> json) {
        return session(test.target(uri).request()).put(Entity.json(json));
    }

    protected Response get(String uri) {
        return session(test.target(uri).request()).get();
    }
    protected Response delete(String uri) {
        return session(test.target(uri).request()).delete();
    }

    protected WebTarget target(String uri) {
        return test.target(uri);
    }

    protected WebTarget target(URI uri) {
        return test.target(uri.getPath());
    }

    private Invocation.Builder session(Invocation.Builder request) {
        return request.header("Authorization", token);
    }

    protected static Map<String, Object> copy(Map<String, Object> src) {
        return Json.fromJson(Json.toJson(src));
    }

    public static class TestContainerFactory implements org.glassfish.jersey.test.spi.TestContainerFactory {
        private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TestContainerFactory.class.getName());
        private ResourceConfig resourceConfig;

        public TestContainerFactory(ResourceConfig resourceConfig) {
            this.resourceConfig = resourceConfig;
        }

        @Override
        public org.glassfish.jersey.test.spi.TestContainer create(URI baseUri, DeploymentContext deploymentContext) {
            return new TestContainer(baseUri, resourceConfig);
        }

        private static class TestContainer implements org.glassfish.jersey.test.spi.TestContainer {
            private final URI baseUri;
            private HttpServer server;
            private static final Logger logger = LoggerFactory.getLogger(TestContainer.class.getName());

            private TestContainer(URI baseUri, ResourceConfig resourceConfig) {
                this.baseUri = baseUri;

                WebappContext context = new WebappContext("Webapp");

                ServletRegistration servlet = context.addServlet("Servlet", new ServletContainer(resourceConfig));
                servlet.addMapping("/*");

                server = GrizzlyHttpServerFactory.createHttpServer(baseUri);
                context.deploy(server);
            }

            @Override
            public ClientConfig getClientConfig() {
                ClientConfig clientConfig = new ClientConfig();
                return clientConfig.register(new LoggingFilter(LOGGER, true));
            }

            @Override
            public URI getBaseUri() {
                return baseUri;
            }

            @Override
            public void start() {
                logger.info("Starting JettyTestContainer...");
                try {
                    server.start();
                } catch (Exception e) {
                    throw new TestContainerException(e);
                }
            }

            @Override
            public void stop() {
                logger.info("Stopping TestContainer...");
                try {
                    this.server.shutdownNow();
                } catch (Exception ex) {
                    logger.info("Error Stopping TestContainer...", ex);
                }
            }
        }
    }

    protected boolean canFindLink(List list, String name, String value) {
        for (Object obj : list) {
            Map map = (Map) obj;
            if (map.getOrDefault("rel", "").equals(name) && map.getOrDefault("uri", "").toString().endsWith(value)) {
                return true;
            }
        }
        return false;
    }

    public static interface SetUp {
        public void before() throws IOException;
    }
}
