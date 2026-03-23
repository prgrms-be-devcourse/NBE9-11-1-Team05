package com.back.orderplz_01.global.initData;

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

    @Bean
    public ApplicationRunner initData() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {

    }
}
