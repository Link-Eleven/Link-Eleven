package com.linkeleven.msa.feed.libs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SuccessResponseDto<T> {
	private final String message;
	private final T data;

	/**
	 * 성공 응답 생성 - 데이터 없이 메시지만 반환
	 */
	public static SuccessResponseDto<Void> success(String message) {
		return SuccessResponseDto.<Void>builder()
			.message(message)
			.build();
	}

	/**
	 * 성공 응답 생성 - 데이터 포함
	 */
	public static <T> SuccessResponseDto<T> success(String message, T data) {
		return SuccessResponseDto.<T>builder()
			.message(message)
			.data(data)
			.build();
	}

	/**
	 * 에러 응답 생성 - 메시지만 반환
	 */
	public static SuccessResponseDto<Void> error(String errorMessage) {
		return SuccessResponseDto.<Void>builder()
			.message(errorMessage)
			.build();
	}
}
