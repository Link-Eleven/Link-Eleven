package com.linkeleven.msa.feed.application.dto;

import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.domain.model.Feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedReadResponseDto {
	private Long feedId;
	private Long userId;
	private Long locationId;
	private String title;
	private String content;
	private Category category;
	private int views;
	private double popularityScore;

	public static FeedReadResponseDto from(Feed feed) {
		return FeedReadResponseDto.builder()
			.feedId(feed.getFeedId())
			.userId(feed.getUserId())
			.locationId(feed.getLocationId())
			.title(feed.getTitle())
			.content(feed.getContent())
			.category(feed.getCategory())
			.views(feed.getViews())
			.popularityScore(feed.getPopularityScore())
			.build();
	}
}
