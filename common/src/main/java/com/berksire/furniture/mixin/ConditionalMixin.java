package com.berksire.furniture.mixin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalMixin {
    String value();

    String forgeRequired() default "";
    String fabricRequired() default "";
}


