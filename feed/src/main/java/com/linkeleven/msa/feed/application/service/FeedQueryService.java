package com.linkeleven.msa.feed.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.domain.service.RecommendationService;
import com.linkeleven.msa.feed.presentation.request.FeedSearchRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedQueryService {

	private final FeedRepository feedRepository;
	private final RecommendationService RecommendationService;

	@Transactional(readOnly = true)
	public Slice<FeedSearchResponseDto> searchFeeds(Long cursorFeedId, String title, String content, String region,
		Category category, List<String> keywordList, Long userId, Pageable pageable) {

		List<String> finalKeywordList = RecommendationService.getRecommendedKeywords(userId, keywordList);

		FeedSearchRequestDto searchRequestDto = FeedSearchRequestDto.builder()
			.title(title)
			.content(content)
			.region(region)
			.category(category)
			.cursorFeedId(cursorFeedId)
			.build();

		return feedRepository.searchFeeds(finalKeywordList, searchRequestDto, pageable);
	}

}
