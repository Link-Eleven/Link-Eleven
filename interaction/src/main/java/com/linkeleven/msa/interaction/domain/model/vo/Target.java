package com.linkeleven.msa.interaction.domain.model.vo;

import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Target {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ContentType contentType;

	@Column(nullable = false)
	private Long targetId;

	public static Target of(ContentType contentType, Long targetId) {
		validate(contentType, targetId);
		return new Target(contentType, targetId);
	}

	private static void validate(ContentType contentType, Long targetId) {
		if (contentType == null) {
			throw new CustomException(ErrorCode.CONTENT_TYPE_CANNOT_BE_NULL);
		}
		if (targetId == null || targetId <= 0) {
			throw new CustomException(ErrorCode.INVALID_TARGET_ID);
		}
	}
}
