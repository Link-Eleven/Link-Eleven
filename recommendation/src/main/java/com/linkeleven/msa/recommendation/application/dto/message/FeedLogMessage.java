package com.linkeleven.msa.recommendation.application.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedLogMessage {
	private Long userId;
	private String feedTitle;
}