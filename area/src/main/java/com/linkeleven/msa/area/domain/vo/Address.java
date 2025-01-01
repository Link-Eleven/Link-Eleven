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
public class Address {

	private String address;

	public static Address from(String address) {
		validate(address);
		return Address.builder()
			.address(address)
			.build();
	}

	private static void validate(String address) {
		if (address == null || address.trim().isEmpty()) {
			throw new CustomException(ErrorCode.ADDRESS_CANNOT_BE_NULL_OR_EMPTY);
		}
		if (address.length() > 100) {
			throw new CustomException(ErrorCode.ADDRESS_LENGTH_EXCEEDED);
		}
	}
}
