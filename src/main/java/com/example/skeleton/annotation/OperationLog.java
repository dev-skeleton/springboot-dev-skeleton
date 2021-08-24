package com.example.skeleton.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String description();

    String entityParam() default "";

    String[] detailParam() default {};

}
