package com.linkeleven.msa.interaction.domain.model.enums;

import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;

public enum ContentType {
	FEED,
	COMMENT,
	REPLY;

	public static ContentType fromString(String input) {
		if (input == null || input.isEmpty()) {
			throw new CustomException(ErrorCode.CONTENT_TYPE_CANNOT_BE_NULL);
		}
		try {
			return ContentType.valueOf(input.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_CONTENT_TYPE);
		}
	}
}
