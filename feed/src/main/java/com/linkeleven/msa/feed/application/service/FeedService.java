package com.linkeleven.msa.feed.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.feed.application.dto.FeedResponseDto;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.presentation.request.FeedCreateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;

	@Transactional
	public FeedResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto) {
		Feed feed = Feed.builder()
			.userId(feedCreateRequestDto.getUserId())
			.areaId(feedCreateRequestDto.getAreaId())
			.locationId(feedCreateRequestDto.getLocationId())
			.title(feedCreateRequestDto.getTitle())
			.content(feedCreateRequestDto.getContent())
			.category(feedCreateRequestDto.getCategory())
			.build();
		Feed savedFeed = feedRepository.save(feed);
		return FeedResponseDto.from(savedFeed);
	}
}
