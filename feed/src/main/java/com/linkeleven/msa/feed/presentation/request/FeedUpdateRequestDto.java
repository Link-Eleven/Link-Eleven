package com.linkeleven.msa.feed.presentation.request;

import com.linkeleven.msa.feed.domain.model.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedUpdateRequestDto {
	private Long feedId;
	private Long userId;
	private String title;
	private String content;
	private Category category;
}
