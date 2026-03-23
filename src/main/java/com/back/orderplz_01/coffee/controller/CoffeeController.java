package com.back.orderplz_01.coffee.controller;

import com.back.orderplz_01.coffee.dto.CoffeeResponseDto;
import com.back.orderplz_01.coffee.dto.CoffeeUpdateRequestDto;
import com.back.orderplz_01.coffee.service.CoffeeService;
import com.back.orderplz_01.coffee.dto.CoffeeDetailDto;
import com.back.orderplz_01.coffee.service.CoffeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    //CUS-02 : 커피 상세내용 보여주기.
    @GetMapping("/{coffeeId}")
    @Operation(summary = "원두 상세 조회", description = "원두 ID를 통해 특정 원두의 상세 정보를 조회합니다.")

    public CoffeeDetailDto getCoffeeDetail(
            @Parameter(description = "조회할 원두의 고유 ID") @PathVariable Long coffeeId) {
        return coffeeService.getCoffeeDetail(coffeeId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 500 에러 대신 프론트엔드에게 400 Bad Request와 직접 작성한 에러 메시지를 보냅니다.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @PatchMapping("/{id}")
    public CoffeeResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody CoffeeUpdateRequestDto requestDto
    ) {
        return coffeeService.update(id, requestDto);
    }
}



