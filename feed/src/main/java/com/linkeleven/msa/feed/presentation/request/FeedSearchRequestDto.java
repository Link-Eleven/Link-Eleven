package com.linkeleven.msa.feed.presentation.request;

import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.model.Category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedSearchRequestDto {

	private String title;
	private String content;
	private Category category;
	private Region region;

}
