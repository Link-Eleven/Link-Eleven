package com.linkeleven.msa.feed.presentation.request;

import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.model.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedCreateRequestDto {
	private Long userId;
	private Long locationId;
	private String title;
	private String content;
	private Category category;
	// private Region region;
	private String region;
	public Region getRegionEnum() { return region != null ? Region.fromFullName(region) : null; }
}
