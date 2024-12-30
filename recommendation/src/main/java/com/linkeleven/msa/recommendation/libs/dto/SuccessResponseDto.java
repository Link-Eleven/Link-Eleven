package com.linkeleven.msa.recommendation.libs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SuccessResponseDto<T> {
	private final String message; // 응답 메시지
	private final T data; // 선택적으로 포함할 데이터

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
