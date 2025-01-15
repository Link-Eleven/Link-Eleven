package com.linkeleven.msa.feed.infrastructure.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JpaAuditorAware implements AuditorAware<Long> {

	private static final Logger logger = LoggerFactory.getLogger(JpaAuditorAware.class);
	private static final Long SYSTEM_USER_ID = 0L; // 기본 사용자 ID (시스템 사용자)

	@Override
	public Optional<Long> getCurrentAuditor() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

		if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
			HttpServletRequest request = servletRequestAttributes.getRequest();
			String userIdStr = request.getHeader("X-User-Id");

			if (userIdStr != null) {
				try {
					return Optional.of(Long.parseLong(userIdStr));
				} catch (NumberFormatException e) {
					logger.warn("적절하지 않은 user ID 포맷 in X-User-Id header: {}", userIdStr, e);
				}
			}
		}

		// 스케줄러 등 요청 외부에서는 기본 사용자 반환
		return Optional.of(SYSTEM_USER_ID);
	}
}