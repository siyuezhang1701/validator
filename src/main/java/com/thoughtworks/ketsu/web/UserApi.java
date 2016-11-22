package com.thoughtworks.ketsu.web;

import com.thoughtworks.ketsu.domain.user.User;

import javax.ws.rs.GET;

public class UserApi {

    private User user;


    public UserApi(User user) {
        this.user = user;
    }

    @GET
    public User findUser(){
        return user;
    }

}
