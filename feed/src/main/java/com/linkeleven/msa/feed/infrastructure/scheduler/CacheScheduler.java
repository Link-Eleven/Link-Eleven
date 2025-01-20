package com.linkeleven.msa.feed.infrastructure.scheduler;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.feed.application.service.FeedService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CacheScheduler {

	private final CacheManager cacheManager;
	private final FeedService feedService;

	@Scheduled(cron = "0 30 23 * * ?")
	public void updateCache() {
		cacheManager.getCache("popularFeeds").clear();
		feedService.updateTopFeed();
	}

	@Scheduled(cron = "0 0 1,4,7,10,13,16,19,22 * * *")
	@Scheduled(cron = "0 30 2,5,8,11,14,17,20 * * *")
	public void updatePartialCache() {
		feedService.updateFeedMetrics();
	}
}