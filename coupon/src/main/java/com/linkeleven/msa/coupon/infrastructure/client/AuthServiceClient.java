package com.linkeleven.msa.coupon.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.linkeleven.msa.coupon.application.dto.UserRoleResponseDto;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
	@PostMapping("/external/users/roles")
	List<UserRoleResponseDto> getUserRoles(@RequestBody List<Long> userIds);
}
