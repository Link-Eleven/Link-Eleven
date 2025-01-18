package com.linkeleven.msa.feed.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.enums.Category;
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
	private Region region;
	private int views;
	private double popularityScore;
	private List<FileResponseDto> files;

	public static FeedReadResponseDto from(Feed feed) {
		return FeedReadResponseDto.builder()
			.feedId(feed.getFeedId())
			.userId(feed.getUserId())
			.locationId(feed.getLocationId())
			.title(feed.getTitle())
			.content(feed.getContent())
			.category(feed.getCategory())
			.region(feed.getRegion())
			.views(feed.getViews())
			.popularityScore(feed.getPopularityScore())
			.files(feed.getFiles().stream().map(FileResponseDto::from).collect(Collectors.toList()))
			.build();
	}
}
