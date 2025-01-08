package com.linkeleven.msa.feed.application.dto;

import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.domain.model.Feed;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedSearchResponseDto {
	private Long feedId;
	private String title;
	private String content;
	private Category category;
	private Region region;

	public static FeedSearchResponseDto from(Feed feed) {
		return FeedSearchResponseDto.builder()
			.feedId(feed.getFeedId())
			.title(feed.getTitle())
			.content(feed.getContent())
			.category(feed.getCategory())
			.region(feed.getRegion())
			.build();
	}
}