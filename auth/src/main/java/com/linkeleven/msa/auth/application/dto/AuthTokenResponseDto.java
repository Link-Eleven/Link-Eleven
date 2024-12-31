package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponseDto {
	String accessToken;

	public static AuthTokenResponseDto from(String accessToken){
		return AuthTokenResponseDto.builder()
			.accessToken(accessToken)
			.build();
	}

}
