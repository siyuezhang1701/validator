package specs.example;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import specs.model.User;

import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(ConcordionRunner.class)
public class UserTest extends ConcordionBaseTest {

    public String importUser(String userId, String username, String email) {
        Map<String, Object> userInfo = new HashMap<String, Object>() {{
            put("id", userId);
            put("email", email);
            put("name", username);
            put("role", "DEV");
        }};
        final Response response = post("/users", cookie, userInfo);
        return response.getStatus() + "";
    }

    public String uniqueUserId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return (instance.getTimeInMillis() + "").substring(4, 12);
    }

    public User getProfile(String userId) {
        final Response response = get(String.format("/users/%s", userId), cookie);
        final Map map = response.readEntity(Map.class);
        return new User(map.get("name")+"", map.get("id")+"");
    }
}
