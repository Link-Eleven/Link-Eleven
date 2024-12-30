package com.linkeleven.msa.notification.libs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExceptionResponseDto<T> {

	private final Integer status;
	private final String message;
	private final T data;

	public static <T> ExceptionResponseDto<T> of(Integer status, String message, T data) {
		return new ExceptionResponseDto<>(status, message, data);
	}
}
