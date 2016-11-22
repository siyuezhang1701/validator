package com.thoughtworks.ketsu.domain.user;

import java.util.Map;
import java.util.Optional;

public interface Users extends Uniqueness{

    Optional<User> findById(long id);

    Optional<User> createUser(Map<String, Object> info);

    Optional<User> findByEmail(String email);
}
