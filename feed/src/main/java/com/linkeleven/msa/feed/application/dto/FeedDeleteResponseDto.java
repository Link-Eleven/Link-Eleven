package com.linkeleven.msa.feed.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FeedDeleteResponseDto {
	private Long feedId;
}
