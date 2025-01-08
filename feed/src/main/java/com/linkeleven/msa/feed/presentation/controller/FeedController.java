package com.linkeleven.msa.feed.presentation.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.application.service.FeedService;
import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.feed.presentation.request.FeedCreateRequestDto;
import com.linkeleven.msa.feed.presentation.request.FeedUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;

	@PostMapping
	public ResponseEntity<SuccessResponseDto<FeedCreateResponseDto>> createFeed(
		@RequestHeader("X-User-Id") Long userId,
		@RequestPart FeedCreateRequestDto feedCreateRequestDto,
		@RequestParam(value = "file", required = false) List<MultipartFile> files) throws IOException {

		FeedCreateResponseDto response = feedService.createFeed(feedCreateRequestDto, files, userId);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 생성 성공", response));
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<FeedUpdateResponseDto>> updateFeed(
		@PathVariable Long feedId,
		@RequestHeader("X-User-Id") Long userId,
		@RequestHeader("X-Role") String userRole,
		@RequestPart FeedUpdateRequestDto feedUpdateRequestDto,
		@RequestParam(value = "file", required = false) List<MultipartFile> files) throws IOException {

		FeedUpdateResponseDto response = feedService.updateFeed(feedId, feedUpdateRequestDto, files, userId, userRole);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 수정 완료", response));
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteFeed(@PathVariable Long feedId,
		@RequestHeader("X-User-Id") Long userId,
		@RequestHeader("X-Role") String userRole) {
		feedService.deleteFeed(feedId, userId, userRole);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 삭제 완료"));
	}

	@GetMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<FeedReadResponseDto>> getDetailFeed(@PathVariable Long feedId) {
		FeedReadResponseDto response = feedService.getDetailsByFeedId(feedId);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 단건 조회 완료", response));
	}

	@GetMapping("/popular")
	public ResponseEntity<SuccessResponseDto<List<FeedTopResponseDto>>> getTopFeed(
		@RequestParam(defaultValue = "3") int limit) {
		List<FeedTopResponseDto> response = feedService.getTopFeed(limit);
		return ResponseEntity.ok(SuccessResponseDto.success("인기 게시글 조회 완료", response));
	}

	@GetMapping
	public Slice<FeedSearchResponseDto> searchFeeds(
		@RequestParam(required = false) String title,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) Region region,
		@RequestParam(required = false) Category category,
		@PageableDefault(size = 10) Pageable pageable) {
		return feedService.searchFeeds(title, content, region, category, pageable);
	}

}