package com.linkeleven.msa.recommendation.domain.model;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLog {
	private Long userId;
	private String feedTitle;
	private LocalDateTime createdAt;

	public static FeedLog of(Long userId, String feedTitle) {
		return FeedLog.builder()
			.userId(userId)
			.feedTitle(feedTitle)
			.createdAt(LocalDateTime.now())
			.build();
	}
}

