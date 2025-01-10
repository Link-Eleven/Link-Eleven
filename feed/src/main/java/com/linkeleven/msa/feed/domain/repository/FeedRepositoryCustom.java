package com.linkeleven.msa.feed.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.presentation.request.FeedSearchRequestDto;

public interface FeedRepositoryCustom {
	Slice<FeedSearchResponseDto> searchFeeds(FeedSearchRequestDto searchRequestDto, Pageable pageable);
}
