package com.example.skeleton.component.appproperty;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "properties")
@PropertySource(value = {"classpath:application.yml", "classpath:application-${PROFILE:dev}.yml"}, ignoreResourceNotFound = true)
public class Property {
    @Value("${passwordExpiration:30}")
    private Long passwordExpiration;
}
