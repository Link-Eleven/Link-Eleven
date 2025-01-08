package com.linkeleven.msa.feed.application.dto;

import com.linkeleven.msa.feed.domain.model.Feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedTopResponseDto {

	private Long feedId;
	private String title;
	private long commentCount;
	private long likeCount;
	private double popularityScore;

	public static FeedTopResponseDto of(Feed feed, long commentCount, long likeCount) {
		return FeedTopResponseDto.builder()
			.feedId(feed.getFeedId())
			.title(feed.getTitle())
			.commentCount(commentCount)
			.likeCount(likeCount)
			.popularityScore(feed.getPopularityScore())
			.build();
	}

}
