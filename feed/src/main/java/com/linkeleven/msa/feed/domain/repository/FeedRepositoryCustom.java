package com.linkeleven.msa.feed.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.presentation.request.FeedSearchRequestDto;

public interface FeedRepositoryCustom {
	Slice<FeedSearchResponseDto> searchFeeds(List<String> keywordList, FeedSearchRequestDto searchRequestDto,
		Pageable pageable);

}
