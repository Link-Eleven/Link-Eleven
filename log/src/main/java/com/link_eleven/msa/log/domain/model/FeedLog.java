package com.link_eleven.msa.log.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLog extends BaseTime {
	private Long userId;
	private String feedTitle;

	public static FeedLog of(Long userId, String feedTitle) {
		return FeedLog.builder()
			.userId(userId)
			.feedTitle(feedTitle)
			.build();
	}
}

