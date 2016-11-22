package com.thoughtworks.ketsu.support;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface TransactionManager {
    <T> void commit(T parameter, Consumer<T> consumer);

    <T> T commit(Supplier<T> consumer);
    <T> void commit(Consumer<T> consumer);
}
