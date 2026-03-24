package coffee.controller;

import com.back.orderplz_01.OrderPlz01Application;
import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(classes = OrderPlz01Application.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class CoffeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Test
    @DisplayName("feat: CUS-02 원두 상세 조회 성공 테스트")
    void getCoffeeDetailTest() throws Exception {
        // 1. Given: 테스트용 데이터를 DB에 실제로 저장함
        Coffee coffee = new Coffee("에티오피아", "맛있는 원두", 15000L, 100L);
        coffeeRepository.save(coffee);

        // 2. When & Then: 실제 URL로 요청을 날리고 결과를 검증함
        mvc.perform(get("/coffees/detail/" + coffee.getId()))
                .andExpect(status().isOk()) // 200 OK가 나오는지?
                .andExpect(jsonPath("$.name").value("에티오피아")) // 이름이 맞는지?
                .andExpect(jsonPath("$.description").value("맛있는 원두")) // 설명이 맞는지?
                .andExpect(jsonPath("$.price").value(15000)) // 가격이 맞는지?
                .andExpect(jsonPath("$.quantity").value(100)); // 이름이 맞는지?

    }

    @Test
    @DisplayName("test: CUS-02 존재하지 않는 원두 조회 시 400 에러")
    void getCoffeeDetailNotFoundTest() throws Exception {
        // 1. Given: DB에 없는 ID (예: 9999번)

        assertThatThrownBy(() -> {
            mvc.perform(get("/coffees/detail/9999"));
        }).hasCauseInstanceOf(EntityNotFoundException.class);
        // MockMvc가 예외를 ServletException으로 감싸서 던지기 때문에 hasCauseInstanceOf를 사용합니다.
    }
}
