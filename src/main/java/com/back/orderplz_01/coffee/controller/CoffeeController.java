package com.back.orderplz_01.coffee.controller;

import com.back.orderplz_01.coffee.dto.CoffeeResponseDto;
import com.back.orderplz_01.coffee.dto.CoffeeUpdateRequestDto;
import com.back.orderplz_01.coffee.service.CoffeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import com.back.orderplz_01.coffee.dto.CoffeeDetailResponse;

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
    public List<CoffeeResponseDto> getAll() {
        return coffeeService.findAll();
    }

    @GetMapping("/{id}")
    public CoffeeResponseDto getOne(@PathVariable Long id) {
        return coffeeService.findById(id);
    }

    //OWN-02 : 판매 할려는 상품을 수정 할 수 있다.
    @PatchMapping("/{id}")
    public CoffeeResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody CoffeeUpdateRequestDto requestDto
    ) {
        return coffeeService.update(id, requestDto);
    }

    //CUS-02 : 커피 상세내용 보여주기.
    @GetMapping("/detail/{coffeeId}")
    @Operation(summary = "원두 상세 조회", description = "원두 ID를 통해 특정 원두의 상세 정보를 조회합니다.")

    public CoffeeDetailResponse getCoffeeDetail(
            @PathVariable Long coffeeId) {
        return coffeeService.getCoffeeDetail(coffeeId);
    }


}



