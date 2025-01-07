package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeleteResponseDto {
	private Long userId;

	public static UserDeleteResponseDto from(Long userId) {
		return UserDeleteResponseDto.builder()
			.userId(userId)
			.build();
	}
}
