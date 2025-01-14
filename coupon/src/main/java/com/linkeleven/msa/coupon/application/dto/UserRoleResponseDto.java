package com.linkeleven.msa.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRoleResponseDto {
	private Long userId;
	private String role;
}
