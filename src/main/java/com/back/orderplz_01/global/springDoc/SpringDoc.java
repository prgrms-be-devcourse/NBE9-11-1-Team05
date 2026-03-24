package com.back.orderplz_01.global.springDoc;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "주문하시조 API", version = "beta", description = "1차 프로젝트 API"))
public class SpringDoc {

    @Bean
    public GroupedOpenApi coffeesApi() {
        return GroupedOpenApi.builder()
                .group("원두 API")
                .pathsToMatch("/coffees/**")
                .build();
    }

    @Bean
    public GroupedOpenApi ordersApi() {
        return GroupedOpenApi.builder()
            .group("주문 API")
            .pathsToMatch("/orders/**")
            .build();
    }
}