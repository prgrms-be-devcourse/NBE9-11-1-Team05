package com.back.orderplz_01.global.apiRes;

public record ApiRes<T>(
	String message,
	T data
) {
}
