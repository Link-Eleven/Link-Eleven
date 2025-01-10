package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValidateIdResponseDto {
	private Long userId;

	public static UserValidateIdResponseDto from (Long userId) {
		return UserValidateIdResponseDto.builder()
			.userId(userId)
			.build();
	}
}
