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
	private Boolean isAnonymous;

	public static UserUpdateAnonymousResponseDto of(Long userId,Boolean isAnonymous) {
		return UserUpdateAnonymousResponseDto.builder()
			.userId(userId)
			.isAnonymous(isAnonymous)
			.build();
	}
}
