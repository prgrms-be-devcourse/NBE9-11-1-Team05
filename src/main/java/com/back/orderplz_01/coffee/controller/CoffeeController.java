package com.back.orderplz_01.coffee.controller;

import com.back.orderplz_01.coffee.dto.CoffeeDetailResponse;
import com.back.orderplz_01.coffee.service.CoffeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffees")
@Tag(name = "CoffeeController", description = "커피 API")
public class CoffeeController {

    private final CoffeeService coffeeService;

    //CUS-02 : 커피 상세내용 보여주기.
    @GetMapping("/{coffeeId}")
    @Operation(summary = "원두 상세 조회", description = "원두 ID를 통해 특정 원두의 상세 정보를 조회합니다.")

    public CoffeeDetailResponse getCoffeeDetail(
            @PathVariable Long coffeeId) {
        return coffeeService.getCoffeeDetail(coffeeId);
    }

}

