package com.linkeleven.msa.feed.presentation.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.application.service.FeedQueryService;
import com.linkeleven.msa.feed.domain.enums.Category;
import com.linkeleven.msa.feed.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedQueryController {

	private final FeedQueryService feedQueryService;

	@GetMapping("/search")
	public ResponseEntity<SuccessResponseDto<Slice<FeedSearchResponseDto>>> searchFeeds(
		@RequestParam(required = false) Long cursorFeedId,
		@RequestParam(required = false) String title,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) String region,
		@RequestParam(required = false) Category category,
		@RequestParam(required = false) List<String> keywordList,
		@RequestParam Long userId,
		@PageableDefault(size = 10) Pageable pageable) {
		Slice<FeedSearchResponseDto> response = feedQueryService.searchFeeds(cursorFeedId, title, content, region,
			category, keywordList, userId, pageable);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 검색 완료", response));
	}

}
