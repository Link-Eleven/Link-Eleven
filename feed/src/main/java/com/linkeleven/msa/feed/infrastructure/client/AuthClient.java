package com.linkeleven.msa.feed.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.linkeleven.msa.feed.application.dto.external.UserValidateIdResponseDto;

@FeignClient(name = "auth-service")
public interface AuthClient {

	@GetMapping("/external/users/{userId}/validate")
	UserValidateIdResponseDto getValidateUserId(@PathVariable("userId") Long userId);


}
