package com.linkeleven.msa.feed.libs.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final com.linkeleven.msa.area.libs.exception.ErrorCode errorCode;
	private final String message;

	public CustomException(com.linkeleven.msa.area.libs.exception.ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}
}
