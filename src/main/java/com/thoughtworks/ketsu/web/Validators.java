package com.thoughtworks.ketsu.web;

import com.thoughtworks.ketsu.domain.user.Uniqueness;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Validators {

    public static void validate(Validator validator, Map<String, Object> info) {
        Optional<String> message = validator.validate(info);
        if (message.isPresent()) {
            throw new BadRequestException(String.join("\n", message.get()));
        }
    }

    public static Validator all(final Validator... validators) {
        return (info) -> {
            List<String> messages = asList(validators).stream().map(validator -> validator.validate(info)).filter(m -> m.isPresent()).map(m -> m.get()).collect(Collectors.toList());
            return messages.size() != 0 ? Optional.of(String.join("\n", messages)) : Optional.empty();
        };
    }

    public interface Validator {
        Optional<String> validate(Map<String, Object> info);
    }

    public static Validators.Validator fieldNotEmpty(String name, String message) {
        return (info) -> info.getOrDefault(name, "").toString().trim().isEmpty() ? Optional.of(message) : Optional.empty();
    }

    public static <Entity, Key> Validator unique(String field, String message,Uniqueness<Key, Entity> range) {
        return (info) -> range.findBy((Key)info.get(field)).map(e -> message);
    }

    interface UniquenessRange<Entity, Key> {
        Optional<Entity> findByUniqueness(Key key);
    }

}
