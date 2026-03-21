package com.back.orderplz_01.global.initData;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Bean
    //work1 실행하기.
    public ApplicationRunner initData() {
        return args -> {
            self.coffee_name();
        };
    }

    @Transactional
    //실행되면 DB에 테스트 데이터 삽입.
    public void coffee_name() {
        if (coffeeRepository.count() == 0) {
            coffeeRepository.save(new Coffee("에티오피아 예가체프",
                    "은은한 자스민 꽃향기와 오렌지 톤의 기분 좋은 산미가 깔끔하게 떨어지는 매력적인 스페셜티입니다.",
                    6500L, 100L));
            coffeeRepository.save(new Coffee("콜롬비아 수프리모",
                    "구운 견과류의 고소함과 짙은 다크 초콜릿의 묵직한 단맛이 훌륭한 밸런스를 이루는 마일드 커피입니다.",
                    6000L, 50L));
            coffeeRepository.save(new Coffee("케냐 AA",
                    "아프리카의 강렬한 태양을 담은 듯 묵직한 바디감과 자몽, 블랙베리가 연상되는 오묘하고 풍부한 과일 향이 일품입니다.",
                    7000L, 80L));
            coffeeRepository.save(new Coffee("과테말라 안티구아",
                    "화산 지대 특유의 쌉싸름하고 스모키한 향미에 캐러멜 같은 부드러운 단맛이 어우러져 깊은 여운을 남깁니다.",
                    6500L, 60L));
        }

    }
}
