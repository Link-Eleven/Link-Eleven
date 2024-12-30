package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
	private Long userId;

	public static AuthResponseDto from(Long userId) {
		return AuthResponseDto.builder()
			.userId(userId)
			.build();
	}
}
