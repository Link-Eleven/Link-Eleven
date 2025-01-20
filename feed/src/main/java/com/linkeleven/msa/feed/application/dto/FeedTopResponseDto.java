package com.linkeleven.msa.feed.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.model.TopFeed;

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
	private Long userId;
	private String title;
	private long commentCount;
	private long likeCount;
	private int views;
	@JsonIgnore
	private double popularityScore;

	public static FeedTopResponseDto of(Feed feed, long commentCount, long likeCount, int views) {
		return FeedTopResponseDto.builder()
			.feedId(feed.getFeedId())
			.userId(feed.getUserId())
			.title(feed.getTitle())
			.commentCount(commentCount)
			.likeCount(likeCount)
			.views(views)
			.popularityScore(feed.getPopularityScore())
			.build();
	}

	public static FeedTopResponseDto toDto(TopFeed topFeed) {
		return FeedTopResponseDto.builder()
			.feedId(topFeed.getFeedId())
			.userId(topFeed.getUserId())
			.title(topFeed.getTitle())
			.commentCount(topFeed.getCommentCount())
			.likeCount(topFeed.getLikeCount())
			.views(topFeed.getViews())
			.popularityScore(topFeed.getPopularityScore())
			.build();
	}
}
