package com.linkeleven.msa.feed.infrastructure.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JpaAuditorAware implements AuditorAware<Long> {

	@Autowired
	private HttpServletRequest request;

	@Override
	public Optional<Long> getCurrentAuditor() {
		String userIdStr = request.getHeader("X-User-Id");

		if(userIdStr != null) {
			return Optional.of(Long.parseLong(userIdStr));
		}

		return Optional.empty();
	}
}
