package com.linkeleven.msa.feed.presentation.controller.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.feed.application.service.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FeedExternalController {

	private final FeedService feedService;

	@GetMapping("/external/feeds/{feedId}")
	public boolean checkFeedExits(@PathVariable Long feedId) {
		return feedService.checkFeedExists(feedId);
	}
}
