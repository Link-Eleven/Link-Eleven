package com.linkeleven.msa.recommendation.infrastructure.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JpaAuditorAware implements AuditorAware<Long> {
	private static final Long SYSTEM_USER_ID = -1L; // 시스템 사용자 ID
	private final ThreadLocal<Long> currentAuditor = new ThreadLocal<>();

	private final HttpServletRequest request;

	@Autowired
	public JpaAuditorAware(HttpServletRequest request) {
		this.request = request;
	}

	public void setCurrentAuditor(Long userId) {
		currentAuditor.set(userId);
	}

	public void clearCurrentAuditor() {
		currentAuditor.remove();
	}

	@Override
	public Optional<Long> getCurrentAuditor() {
		Long auditor = currentAuditor.get();
		if (auditor != null) {
			return Optional.of(auditor);
		}
		try {
			String userIdStr = request.getHeader("X-User-Id");
			if (userIdStr != null) {
				return Optional.of(Long.parseLong(userIdStr));
			}
		} catch (Exception e) {
			return Optional.of(SYSTEM_USER_ID);
		}
		return Optional.of(SYSTEM_USER_ID);
	}
}