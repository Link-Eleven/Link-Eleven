package com.linkeleven.msa.gateway.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.linkeleven.msa.gateway.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.gateway.application.service.UserService;

@FeignClient(name="auth-service")
public interface UserClient extends UserService {

	@GetMapping("/external/users/{userId}/role")
	UserRoleResponseDto getUserRole(@PathVariable("userId") Long userId);
}
