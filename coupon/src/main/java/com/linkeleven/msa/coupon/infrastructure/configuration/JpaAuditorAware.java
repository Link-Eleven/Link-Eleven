package com.linkeleven.msa.coupon.infrastructure.configuration;

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