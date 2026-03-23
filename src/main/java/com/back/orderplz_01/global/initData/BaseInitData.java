package com.back.orderplz_01.global.initData;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;

    private final CoffeeRepository coffeeRepository;

    @Bean
    public ApplicationRunner initData() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (coffeeRepository.count() > 0) return;

        coffeeRepository.save(new Coffee(
                "에티오피아 예가체프",
                "꽃향과 산미가 특징인 원두입니다.",
                18000L,
                12L
        ));

        coffeeRepository.save(new Coffee(
                "콜롬비아 수프리모",
                "고소하고 밸런스가 좋은 원두입니다.",
                16000L,
                8L
        ));

        coffeeRepository.save(new Coffee(
                "브라질 산토스",
                "부드럽고 견과류 향이 나는 원두입니다.",
                15000L,
                0L
        ));
    }
}