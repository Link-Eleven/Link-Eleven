package com.linkeleven.msa.feed.presentation.controller.external;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.feed.application.dto.external.PopularFeedResponseDto;
import com.linkeleven.msa.feed.application.service.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FeedExternalController {

	private final FeedService feedService;

	@GetMapping("/external/feeds/{feedId}/users/{userId}")
	public boolean checkFeedExists(@PathVariable Long feedId, @PathVariable Long userId) {
		return feedService.checkFeedExists(feedId, userId);
	}

	@GetMapping("/external/feeds/popular")
	public List<PopularFeedResponseDto> getPopularFeeds() {
		return feedService.getPopularFeedForCoupon();
	}
}
