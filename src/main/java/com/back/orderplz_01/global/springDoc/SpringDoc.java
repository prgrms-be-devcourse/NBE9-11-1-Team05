package com.back.orderplz_01.global.springDoc;

import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@Configuration
@OpenAPIDefinition(info = @Info(title = "주문하시조 API", version = "beta", description = "1차 프로젝트 API"))
public class SpringDoc {

    // 1. 고객용 API 그룹 설정
    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("고객용 API") // 우측 상단 선택창에 보여질 이름
                .pathsToMatch("/api/coffees/**")       // 이 그룹에 묶어줄 API 주소 패턴 (컨트롤러 주소에 맞춤)
                .build();
    }

    // Swagger 테스트 용(임시)
    @Bean
    public GroupedOpenApi ordersApi() {
        return GroupedOpenApi.builder()
                .group("업주용 API")
                .pathsToMatch("/api/orders/**")
                .build();
    }
}