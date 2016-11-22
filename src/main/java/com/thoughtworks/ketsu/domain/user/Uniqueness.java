package com.thoughtworks.ketsu.domain.user;

import java.util.Optional;

public interface Uniqueness<Key, Entity> {
    Optional<Entity> findBy(Key key);
}
