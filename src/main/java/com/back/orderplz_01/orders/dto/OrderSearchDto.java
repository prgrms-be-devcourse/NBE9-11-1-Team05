package com.back.orderplz_01.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.back.orderplz_01.orders.entity.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CUS-09 주문 검색 API 요청 응답.
 */
@Schema(description = "CUS-09 주문 검색")
public final class OrderSearchDto {

	private OrderSearchDto() {
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(name = "CUS-09-OrderSearchRequest")
	public static class Request {
		@Schema(example = "email@example.com")
		private String email;
		@Schema(example = "서울시 강남구 테헤란로 123")
		private String address;
		@Schema(example = "12345")
		private String zipCode;
	}

	@Getter
	@AllArgsConstructor
	@Schema(name = "CUS-09-OrderSearchResponse")
	public static class Response {
		@Schema(description = "일치 주문이 없을 때 안내. 조회 성공 시 null")
		private String message;
		private List<OrderSummary> orders;
	}

	@Getter
	@AllArgsConstructor
	@Schema(name = "CUS-09-OrderSummary")
	public static class OrderSummary {
		@Schema(description = "주문번호 (Orders.id)")
		private Long orderId;
		@Schema(description = "주문일자 (BaseEntity.createDate)")
		private LocalDateTime orderDate;
		@Schema(description = "주문상태 (Orders.orderStatus)")
		private OrderStatus orderStatus;
		@Schema(description = "주문내역 (OrdersItem 목록)")
		private List<LineItem> orderLines;
		@Schema(description = "배송지 주소 (Orders.address)")
		private String address;
		@Schema(description = "우편번호 (Orders.zipCode)")
		private String zipCode;
		@Schema(description = "총 가격 (Orders.totalAmount)")
		private Long totalAmount;
	}

	@Getter
	@AllArgsConstructor
	@Schema(name = "CUS-09-OrderLineItem")
	public static class LineItem {
		@Schema(description = "원두(상품)명 (Coffee.name)")
		private String coffeeName;
		@Schema(description = "수량 (OrdersItem.quantity)")
		private Long quantity;
		@Schema(description = "가격 (OrdersItem.price)")
		private Long price;
	}

	@Schema(name = "CUS-09-ApiError", description = "JSON 오류 응답 (HTTP 400)")
	public record ApiError(@Schema(description = "오류 안내", example = "이메일을 입력해주세요.") String message) {
	}
}

