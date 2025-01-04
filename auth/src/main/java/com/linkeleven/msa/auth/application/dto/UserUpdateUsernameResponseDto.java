package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateUsernameResponseDto {
	private Long userId;

	public static UserUpdateUsernameResponseDto from(Long userId) {
		return UserUpdateUsernameResponseDto.builder()
			.userId(userId)
			.build();

	}
}
