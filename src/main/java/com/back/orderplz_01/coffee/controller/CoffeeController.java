package com.back.orderplz_01.coffee.controller;

import com.back.orderplz_01.coffee.dto.response.CoffeeResponseDto;
import com.back.orderplz_01.coffee.dto.request.CoffeeUpdateRequestDto;
import com.back.orderplz_01.coffee.service.CoffeeService;
import com.back.orderplz_01.global.apiRes.ApiRes;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.back.orderplz_01.coffee.dto.response.CoffeeDetailResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coffees")
@Tag(name = "CoffeeController", description = "커피 API")
public class CoffeeController {

    private final CoffeeService coffeeService;

    @GetMapping
    @Operation(summary = "전체 상품 조회", description = "등록된 전체 원두 상품 목록을 조회합니다.")
    public ResponseEntity<ApiRes<List<CoffeeResponseDto>>> getAll() {
        List<CoffeeResponseDto> coffees = coffeeService.findAll();
        return ResponseEntity.ok(new ApiRes<>("전체 상품 조회 성공", coffees));
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 단건 조회", description = "상품 ID로 원두 상품 1건을 조회합니다.")
    public ResponseEntity<ApiRes<CoffeeResponseDto>> getOne(@PathVariable Long id) {
        CoffeeResponseDto coffee = coffeeService.findById(id);
        return ResponseEntity.ok(new ApiRes<>("상품 조회 성공", coffee));
    }

    // OWN-02 : 판매 할 상품을 수정할 수 있다.
    @PatchMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품 ID로 원두 상품 정보를 수정합니다.")
    public ResponseEntity<ApiRes<CoffeeResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody CoffeeUpdateRequestDto requestDto
    ) {
        CoffeeResponseDto updatedCoffee = coffeeService.update(id, requestDto);
        return ResponseEntity.ok(new ApiRes<>("상품 수정 성공", updatedCoffee));
    }

    //CUS-02 : 커피 상세내용 보여주기.
    @GetMapping("/detail/{coffeeId}")
    @Operation(summary = "원두 상세 조회", description = "원두 ID를 통해 특정 원두의 상세 정보를 조회합니다.")

    public CoffeeDetailResponse getCoffeeDetail(
            @PathVariable Long coffeeId) {
        return coffeeService.getCoffeeDetail(coffeeId);
    }


}



