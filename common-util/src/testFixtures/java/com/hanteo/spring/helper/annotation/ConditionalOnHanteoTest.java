package com.hanteo.spring.helper.annotation;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Tag("test")
public @interface ConditionalOnHanteoTest {}
