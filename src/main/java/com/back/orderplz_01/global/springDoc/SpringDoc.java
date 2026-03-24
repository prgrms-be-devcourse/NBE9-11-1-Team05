package com.back.orderplz_01.global.springDoc;

import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@Configuration
@OpenAPIDefinition(info = @Info(title = "주문하시조 API", version = "beta", description = "1차 프로젝트 API"))
public class SpringDoc {

    // CUS-09 주문조회 API
    @Bean
    public GroupedOpenApi orderSearchApi() {
        return GroupedOpenApi.builder()
                .group("CUS-09 내 주문조회 API")
                .pathsToMatch("/orders/search")
                .build();
    }

}