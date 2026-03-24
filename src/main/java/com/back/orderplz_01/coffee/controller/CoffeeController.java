package com.back.orderplz_01.coffee.controller;

import com.back.orderplz_01.coffee.dto.CoffeeResponseDto;
import com.back.orderplz_01.coffee.dto.CoffeeUpdateRequestDto;
import com.back.orderplz_01.coffee.service.CoffeeService;
import com.back.orderplz_01.global.apiRes.ApiRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
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
    //API 응답 적용 완료
    @GetMapping("/detail/{coffeeId}")
    @Operation(summary = "원두 상세 조회", description = "원두 ID를 통해 특정 원두의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "원두 상세 조회 성공"),
            @ApiResponse(responseCode = "500", description = "해당 ID의 원두가 존재하지 않음 (예: 999번 원두는 존재하지 않습니다.)")
    })
    public ResponseEntity<ApiRes<CoffeeDetailResponse>> getCoffeeDetail(
            @Parameter(description = "조회할 원두의 고유 ID", example = "1")
            @PathVariable Long coffeeId) {
        CoffeeDetailResponse data = coffeeService.getCoffeeDetail(coffeeId);
        return ResponseEntity.ok(new ApiRes<>("원두 상세 조회 성공", data));
    }


}



