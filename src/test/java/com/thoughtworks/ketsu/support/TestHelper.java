package com.thoughtworks.ketsu.support;

import com.thoughtworks.ketsu.domain.user.User;


import java.util.HashMap;
import java.util.Map;

public class TestHelper {

    public static Map<String, Object> userMap(String email, String password) {
        return new HashMap<String, Object>() {{
            put("email", email);
            put("password", password);
        }};
    }

}
