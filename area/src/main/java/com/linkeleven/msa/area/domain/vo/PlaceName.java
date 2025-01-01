package com.linkeleven.msa.area.domain.vo;

import com.linkeleven.msa.area.libs.exception.CustomException;
import com.linkeleven.msa.area.libs.exception.ErrorCode;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PlaceName {

	private String placeName;


	public static PlaceName from(String placeName) {
		validate(placeName);
		return PlaceName.builder()
			.placeName(placeName)
			.build();
	}

	private static void validate(String placeName) {
		if (placeName == null || placeName.trim().isEmpty()) {
			throw new CustomException(ErrorCode.PLACE_NAME_CANNOT_BE_NULL_OR_EMPTY);
		}
		if (placeName.length() > 50) {
			throw new CustomException(ErrorCode.PLACE_NAME_LENGTH_EXCEEDED);
		}
	}

}
