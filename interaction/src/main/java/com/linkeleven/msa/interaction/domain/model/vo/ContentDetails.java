package com.linkeleven.msa.interaction.domain.model.vo;

import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentDetails {

	private String content;

	private Long userId;

	private String username;

	public static ContentDetails of(String content, Long userId, String username) {
		validate(content, userId, username);
		return new ContentDetails(content, userId, username);
	}

	private static void validate(String content, Long userId, String username) {
		if (content == null || content.isBlank()) {
			throw new CustomException(ErrorCode.CONTENT_CANNOT_BE_NULL_OR_EMPTY);
		}
		if (content.length() > 100) {
			throw new CustomException(ErrorCode.CONTENT_TOO_LONG);
		}
		if (userId == null || userId <= 0) {
			throw new CustomException(ErrorCode.INVALID_USERID);
		}
		if (username == null || username.isBlank()) {
			throw new CustomException(ErrorCode.USERNAME_CANNOT_BE_NULL_OR_EMPTY);
		}
	}

}
