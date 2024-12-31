package com.linkeleven.msa.feed.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.feed.application.dto.FeedResponseDto;
import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.libs.exception.CustomException;
import com.linkeleven.msa.feed.libs.exception.ErrorCode;
import com.linkeleven.msa.feed.presentation.request.FeedCreateRequestDto;
import com.linkeleven.msa.feed.presentation.request.FeedUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;

	@Transactional
	public FeedResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto) {
		Feed feed = Feed.of(
			feedCreateRequestDto.getUserId(),
			feedCreateRequestDto.getLocationId(),
			feedCreateRequestDto.getTitle(),
			feedCreateRequestDto.getContent(),
			feedCreateRequestDto.getCategory()
		);
		Feed savedFeed = feedRepository.save(feed);
		return FeedResponseDto.from(savedFeed);
	}

	@Transactional
	public FeedResponseDto updateFeed(FeedUpdateRequestDto feedUpdateRequestDto) {
		Feed feed = feedRepository.findById(feedUpdateRequestDto.getFeedId())
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		feed.update(
			feedUpdateRequestDto.getTitle(),
			feedUpdateRequestDto.getContent(),
			feedUpdateRequestDto.getCategory()
		);
		Feed updatedFeed = feedRepository.save(feed);
		return FeedResponseDto.from(updatedFeed);
	}

	public boolean checkFeedExists(Long feedId) {
		return feedRepository.existsById(feedId);
	}
}
