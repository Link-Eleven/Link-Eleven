package com.linkeleven.msa.feed.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
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
	public FeedCreateResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto) {
		Feed feed = Feed.of(
			feedCreateRequestDto.getUserId(),
			feedCreateRequestDto.getLocationId(),
			feedCreateRequestDto.getTitle(),
			feedCreateRequestDto.getContent(),
			feedCreateRequestDto.getCategory()
		);
		Feed savedFeed = feedRepository.save(feed);
		return FeedCreateResponseDto.from(savedFeed);
	}

	@Transactional
	public FeedUpdateResponseDto updateFeed(Long feedId, FeedUpdateRequestDto feedUpdateRequestDto) {
		Feed feed = feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		feed.update(
			feedUpdateRequestDto.getTitle(),
			feedUpdateRequestDto.getContent(),
			feedUpdateRequestDto.getCategory()
		);
		Feed updatedFeed = feedRepository.save(feed);
		return FeedUpdateResponseDto.from(updatedFeed);
	}

	@Transactional
	public void deleteFeed(Long feedId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		feed.delete();
		feedRepository.save(feed);
	}

	@Transactional
	public FeedReadResponseDto getDetailsByFeedId(Long feedId) {
		boolean exists = feedRepository.existsByFeedId(feedId);
		if (!exists) {
			throw new CustomException(ErrorCode.FEED_NOT_FOUND);
		}

		feedRepository.incrementViews(feedId);

		Feed feed = feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));
		return FeedReadResponseDto.from(feed);
	}

	public boolean checkFeedExists(Long feedId) {
		return feedRepository.existsById(feedId);
	}
}
