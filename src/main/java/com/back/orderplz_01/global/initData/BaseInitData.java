package com.back.orderplz_01.global.initData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.coffee.repository.CoffeeRepository;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class BaseInitData {

	@Autowired
	@Lazy
	private BaseInitData self;

	@Autowired
	private CoffeeRepository coffeeRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Bean
	//work1 실행하기.
	public ApplicationRunner initData() {
		return args -> {
			self.initCoffees();
			self.initOrders();
		};
	}

	// 커피 재고 등록
	@Transactional
	public void initCoffees() {
		// 정상 재고 커피 (일반 주문 / 묶음 배송 테스트용)
		if (coffeeRepository.count() == 0) {
			Coffee ethiopia = Coffee.create(
				"에티오피아 예가체프",
				"과일향이 풍부한 워시드 원두",
				15000L,
				100L
			);

			Coffee brazil = Coffee.create(
				"브라질 산토스",
				"고소하고 부드러운 내추럴 원두",
				12000L,
				50L
			);

			Coffee colombia = Coffee.create(
				"콜롬비아 수프리모",
				"재고 부족 테스트용",
				13000L,
				1L
			);

			Coffee guatemala = Coffee.create(
				"과테말라 안티구아",
				"쌉싸름하고 스모키한 원두",
				12000L,
				50L
			);

			coffeeRepository.saveAll(List.of(ethiopia, brazil, colombia, guatemala));

			log.info("=== 커피 등록 완료 ===");
		}
	}

	// ── 2. 묶음 배송 테스트용 기존 주문 등록 ──────────────────────
	// 오후 2시 이전에 앱을 실행해야 묶음 배송 로직이 동작합니다.
	// 전날 오후 2시 ~ 오늘 오후 2시 사이의 PROCESSING 주문을 넣어둡니다.
	@Transactional
	public void initOrders() {
		if (ordersRepository.count() == 0) {
			Coffee ethiopia = coffeeRepository.findAll().stream()
				.filter(c -> c.getName().equals("에티오피아 예가체프"))
				.findFirst()
				.orElseThrow();

			// 묶음 배송 대상이 될 기존 주문
			// - 동일한 email / address / zipCode 로 새 주문이 들어오면 묶임
			Orders existingOrder = Orders.create(
				"test@example.com",
				"서울시 강남구 테헤란로 1길",
				"06234"
			);

			existingOrder.addOrderItem(2L, ethiopia.getPrice(), ethiopia);
			ethiopia.decreaseQuantity(2L); // 재고도 함께 차감

			ordersRepository.save(existingOrder);

			log.info("=== 묶음 배송 대상 주문 등록 완료 ===");
		}
	}
}
