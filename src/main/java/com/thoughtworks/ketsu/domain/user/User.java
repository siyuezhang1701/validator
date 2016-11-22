package com.thoughtworks.ketsu.domain.user;

public class User {
    private long id;
    private String email;

    public User(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }
}
