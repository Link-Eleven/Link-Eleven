package com.linkeleven.msa.recommendation.infrastructure.messaging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedLogMessage {
	private Long userId;
	private String feedTitle;
}