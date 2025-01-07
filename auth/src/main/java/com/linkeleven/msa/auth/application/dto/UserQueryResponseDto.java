package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryResponseDto {
	private Long userId;
	private String username;
	private String role;
	private Boolean isAnonymous;
	private Boolean isCouponIssued;
}
