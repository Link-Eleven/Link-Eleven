package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateAnonymousResponseDto {
	private Long userId;

	public static UserUpdateAnonymousResponseDto from(Long userId) {
		return UserUpdateAnonymousResponseDto.builder()
			.userId(userId)
			.build();
	}
}
