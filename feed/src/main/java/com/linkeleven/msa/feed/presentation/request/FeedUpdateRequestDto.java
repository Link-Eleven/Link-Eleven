package com.linkeleven.msa.feed.presentation.request;

import com.linkeleven.msa.feed.domain.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedUpdateRequestDto {
	private Long userId;
	private String title;
	private String content;
	private Category category;
}
