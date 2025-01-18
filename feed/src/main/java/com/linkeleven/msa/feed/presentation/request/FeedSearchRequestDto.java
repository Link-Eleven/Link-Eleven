package com.linkeleven.msa.feed.presentation.request;

import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedSearchRequestDto {

	private String title;
	private String content;
	private Category category;
	private String region;
	private Long cursorFeedId;

	public Region getRegionEnum() {
		return region != null ? Region.fromFullName(region) : null;
	}
}
