package com.linkeleven.msa.coupon.infrastructure.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JpaAuditorAware implements AuditorAware<Long> {
	private static final Long SYSTEM_USER_ID = -1L; // 시스템 사용자 ID
	private final ThreadLocal<Long> currentAuditor = new ThreadLocal<>();

	@Autowired
	private HttpServletRequest request;

	public void setCurrentAuditor(Long userId) {
		currentAuditor.set(userId);
	}

	public void clearCurrentAuditor() {
		currentAuditor.remove();
	}

	@Override
	public Optional<Long> getCurrentAuditor() {
		// ThreadLocal에 저장된 사용자 ID가 있는지 먼저 확인
		Long auditor = currentAuditor.get();
		if (auditor != null) {
			return Optional.of(auditor);
		}

		// HTTP 요청이 있는 경우 헤더에서 사용자 ID 확인
		try {
			String userIdStr = request.getHeader("X-User-Id");
			if (userIdStr != null) {
				return Optional.of(Long.parseLong(userIdStr));
			}
		} catch (Exception e) {
			// HTTP 요청이 없는 경우 (스케줄러 컨텍스트) 시스템 사용자 ID 반환
			return Optional.of(SYSTEM_USER_ID);
		}

		return Optional.of(SYSTEM_USER_ID);
	}
}