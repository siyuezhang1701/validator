package com.thoughtworks.ketsu.support;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Description {
    String value();
}
