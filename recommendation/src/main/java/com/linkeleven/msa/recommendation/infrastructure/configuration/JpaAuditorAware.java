package com.linkeleven.msa.recommendation.infrastructure.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JpaAuditorAware implements AuditorAware<Long> {

	private final HttpServletRequest request;

	@Autowired
	public JpaAuditorAware(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Optional<Long> getCurrentAuditor() {
		String userIdStr = request.getHeader("X-User-Id");
		if (userIdStr != null) {
			try {
				return Optional.of(Long.parseLong(userIdStr));
			} catch (NumberFormatException e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
}
