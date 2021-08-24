package com.example.skeleton.component.appproperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class Accessor implements ApplicationRunner {

    static private final Logger logger = LoggerFactory.getLogger(Accessor.class);
    static public Property AppProperty;
    private final Property property;

    public Accessor(Property property) {
        this.property = property;
        AppProperty = property;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("app properties ==> {}", this.property);
    }
}
