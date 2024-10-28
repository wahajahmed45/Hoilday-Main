package org.helmo.HolyD.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


//@Configuration
public class SpringFoxConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final List<String> DEFAULT_INCLUDE_PATTERN = Arrays.asList("/vacance", "/REST_AHME_VERD_WABO/vacance",
            "/sse", "/REST_AHME_VERD_WABO/sse");

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .apiInfo(apiInfo())
                .tags(new Tag("user", "Sign service & tools"),
                        new Tag("vacance", "Vacance service"),
                        new Tag("sse", "SSE service"))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.helmo.HolyD.controllers"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HolyD REST API")
                .version("1.0.0")
                .description("API for HolyD apps.")
                .build();
    }
    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext ->
                        DEFAULT_INCLUDE_PATTERN.stream().anyMatch(
                                pattern -> operationContext.requestMappingPattern().startsWith(pattern)
                        )
                )
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }
}