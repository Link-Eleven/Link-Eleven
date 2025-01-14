package com.link_eleven.msa.log.domain.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeminiKeywordHolder extends BaseTime {
	private List<String> keywords;
	private Long userId;

	public static GeminiKeywordHolder of(List<String> keywords, Long userId) {
		return GeminiKeywordHolder.builder()
			.keywords(keywords)
			.userId(userId)
			.build();
	}
}