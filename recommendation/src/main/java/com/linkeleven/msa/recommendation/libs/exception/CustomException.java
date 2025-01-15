package com.linkeleven.msa.recommendation.libs.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String message;

	public CustomException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}
}
