package com.back.orderplz_01.coffee.controller;

import com.back.orderplz_01.coffee.dto.CoffeeResponseDto;
import com.back.orderplz_01.coffee.dto.CoffeeUpdateRequestDto;
import com.back.orderplz_01.coffee.service.CoffeeService;
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

    @PatchMapping("/{id}")
    public CoffeeResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody CoffeeUpdateRequestDto requestDto
    ) {
        return coffeeService.update(id, requestDto);
    }
}