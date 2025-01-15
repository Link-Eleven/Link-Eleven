package com.linkeleven.msa.feed.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.infrastructure.client.RecommendationClient;
import com.linkeleven.msa.feed.presentation.request.FeedSearchRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedSearchService {

	private final FeedRepository feedRepository;
	private final RecommendationClient recommendationClient;

	@Transactional(readOnly = true)
	public Slice<FeedSearchResponseDto> searchFeeds(Long cursorFeedId, String title, String content, String region,
		Category category, Pageable pageable) {

		FeedSearchRequestDto searchRequestDto = FeedSearchRequestDto.builder()
			.title(title)
			.content(content)
			.region(region)
			.category(category)
			.cursorFeedId(cursorFeedId)
			.build();

		return feedRepository.searchFeeds(searchRequestDto, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<FeedSearchResponseDto> searchFeedsByKeywords(Long userId, Pageable pageable) {

		List<String> keywords = recommendationClient.getRecommendationKeywords(userId);

		return feedRepository.searchFeedsByKeywords(keywords, pageable);
	}
}
