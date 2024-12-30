package com.linkeleven.msa.feed.application.dto;

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
	private Long areaId;
	private String title;
	private String content;
	private String category;
	private int views;
	private double popularityScore;
}
