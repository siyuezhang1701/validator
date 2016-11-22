package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Map;

@RunWith(ConcordionRunner.class)
@Ignore
public class ConcordionBaseTest {
    Client client;
    String cookie;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
    }

    public String getUrl(String path) {
        String entryPoint = System.getenv("ENTRYPOINT") != null ?
                System.getenv("ENTRYPOINT") : "localhost:8088";
        if (entryPoint.startsWith("http"))
            return entryPoint + path;
        return "http://" + entryPoint + path;
    }

    public Response get(String uri, String cookie){
        return client.target(getUrl(uri))
                .request()
                .header("Cookie", cookie)
                .get();
    }

    public Response post(String uri, String cookie, Map<String, Object> json) {
        return client.target(getUrl(uri))
                .request()
                .header("Cookie", cookie)
                .post(Entity.json(json));
    }

    public Response delete(String uri, String cookie) {
        return client.target(getUrl(uri)).request().header("Cookie", cookie).delete();
    }
}
