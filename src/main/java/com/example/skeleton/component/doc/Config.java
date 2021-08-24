package com.example.skeleton.component.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

@Configuration
@ConfigurationProperties("swagger")
//@SecurityScheme(type = SecuritySchemeType.HTTP, name = "bearerAuth", bearerFormat = "JWT", scheme = "bearer")
public class Config {

    private final BuildProperties buildProperties;
    private final GitProperties gitProperties;

    public Config(BuildProperties buildProperties, GitProperties gitProperties) {
        this.buildProperties = buildProperties;
        this.gitProperties = gitProperties;
    }

    @Bean
    public OpenAPI customOpenApi() {
        var x = new OpenAPI().info(new Info()
                .title(buildProperties.getName())
                .version(buildProperties.getVersion())
                .description(String.format("%s-%s, commit at %s, compile at %s", gitProperties.getBranch(), gitProperties.getShortCommitId(), LocalDateTime.ofInstant(gitProperties.getCommitTime(), ZoneId.systemDefault()), LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault())))
                .contact(new Contact().email("ricl@un-net.com"))
                .license(new License().name("private"))).security(new ArrayList<>());

        Security(x);
        return x;
    }

    private void Security(OpenAPI api) {
        final String securitySchemeName = "bearerAuth";
        api.addSecurityItem(
                new SecurityRequirement().addList(securitySchemeName)).components(
                        new Components().addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

}
