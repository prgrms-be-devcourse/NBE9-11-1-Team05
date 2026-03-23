package com.back.orderplz_01.coffee.controller;

import com.back.orderplz_01.coffee.dto.CoffeeDetailDto;
import com.back.orderplz_01.coffee.service.CoffeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public CoffeeDetailDto getCoffeeDetail(
            @Parameter(description = "조회할 원두의 고유 ID")@PathVariable Long coffeeId) {
        return coffeeService.getCoffeeDetail(coffeeId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 500 에러 대신 프론트엔드에게 400 Bad Request와 직접 작성한 에러 메시지를 보냅니다.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

