package com.linkeleven.msa.feed.presentation.controller.external;

import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Boolean> checkFeedExits(@PathVariable Long feedId){
		boolean exits = feedService.checkFeedExists(feedId);
		return ResponseEntity.ok(exits);
	}
}
