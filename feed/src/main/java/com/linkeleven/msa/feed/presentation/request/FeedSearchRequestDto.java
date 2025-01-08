package com.linkeleven.msa.feed.presentation.request;

import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.model.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedSearchRequestDto {

	private String title;
	private String content;
	private Category category;
	private Region region;

}
