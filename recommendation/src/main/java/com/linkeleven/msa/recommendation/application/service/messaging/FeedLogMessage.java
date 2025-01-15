package com.linkeleven.msa.recommendation.application.service.messaging;

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