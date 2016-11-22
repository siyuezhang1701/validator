package com.thoughtworks.ketsu.web;

import com.thoughtworks.ketsu.domain.CurrentUser;
import com.thoughtworks.ketsu.domain.user.User;
import com.thoughtworks.ketsu.domain.user.Users;
import com.thoughtworks.ketsu.support.TestHelper;
import com.thoughtworks.ketsu.web.jersey.RoutesFeature;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class UsersApiTest extends JerseyTest {

    private Users users = mock(Users.class);
    private CurrentUser currentUser = mock(CurrentUser.class);
    private String email;
    private String password;

    @Override
    protected Application configure() {
        return new ResourceConfig(UsersApi.class)
                .register(RoutesFeature.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(users).to(Users.class);
                    }
                });
    }

    @Override
    @Before
    public void setUp() throws Exception {
        email = "xxx@xxx.com";
        password = "pass";
        reset(users);
        reset(currentUser);
        super.setUp();
    }

    @Test
    public void should_return_404_when_user_not_exist() throws Exception {
        when(users.findById(anyInt())).thenReturn(Optional.empty());
        Response response = target("/users/1").request().get();

        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_return_201_when_create_user() throws Exception {
        User user = new User(1, email);
        when(users.createUser(anyMap())).thenReturn(Optional.ofNullable(user));
        when(users.findByEmail(anyString())).thenReturn(Optional.empty());

        Response response = target("/users").request().post(Entity.json(TestHelper.userMap(email, password)));

        assertThat(response.getStatus(), is(201));
        assertThat(response.getLocation().toString().contains("users/1"), is(true));
    }

    @Test
    public void should_return_400_when_email_existed() throws Exception {
        User user = new User(1, email);
        when(users.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        Response response = target("/users").request().post(Entity.json(TestHelper.userMap(email, password)));

        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void should_return_400_when_data_not_completed() throws Exception {

        Response response = target("/users").request().post(Entity.json(new HashMap<>()));

        assertThat(response.getStatus(), is(400));
    }



}
