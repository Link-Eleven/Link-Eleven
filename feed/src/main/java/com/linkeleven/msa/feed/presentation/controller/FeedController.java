package com.linkeleven.msa.feed.presentation.controller;

import java.io.IOException;
import java.util.List;

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
import com.linkeleven.msa.feed.application.dto.FeedResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.application.service.FeedService;
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
		@RequestHeader("Authorization") String token,
		@RequestPart FeedCreateRequestDto feedCreateRequestDto,
		@RequestParam(value = "file", required = false) List<MultipartFile> files) throws IOException {

		FeedCreateResponseDto response = feedService.createFeed(feedCreateRequestDto, files, token);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 생성 성공", response));
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<FeedUpdateResponseDto>> updateFeed(
		@PathVariable Long feedId,
		@RequestHeader("Authorization") String token,
		@RequestPart FeedUpdateRequestDto feedUpdateRequestDto,
		@RequestParam(value = "file", required = false) List<MultipartFile> files) throws IOException {

		FeedUpdateResponseDto response = feedService.updateFeed(feedId, feedUpdateRequestDto, files, token);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 수정 완료", response));
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteFeed(@PathVariable Long feedId,
		@RequestHeader("Authorization") String token) {
		feedService.deleteFeed(feedId, token);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 삭제 완료"));
	}

	@GetMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<FeedReadResponseDto>> getDetailFeed(@PathVariable Long feedId) {
		FeedReadResponseDto response = feedService.getDetailsByFeedId(feedId);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 단건 조회 완료", response));
	}

	@GetMapping("/popular")
	public ResponseEntity<SuccessResponseDto<List<FeedTopResponseDto>>> getTopFeed(@RequestParam(defaultValue = "3") int limit) {
		List<FeedTopResponseDto> response = feedService.getTopFeed(limit);
		return  ResponseEntity.ok(SuccessResponseDto.success("인기 게시글 조회 완료", response));
	}

	@GetMapping
	public ResponseEntity<SuccessResponseDto<List<FeedReadResponseDto>>> searchFeeds(@RequestParam String keyword) {
		// TODO : 게시글 서치 실제 구현
		// 더미 데이터 검색 로직
		List<FeedReadResponseDto> responses = List.of(
			FeedReadResponseDto.builder()
				.feedId(1L)
				.title("Title 1")
				.content("Content 1")
				.category(Category.valueOf("HOTEL"))
				.views(15)
				.popularityScore(4.0)
				.build(),
			FeedReadResponseDto.builder()
				.feedId(2L)
				.title("Title 2")
				.content("Content 2")
				.category(Category.valueOf("RESTAURANT"))
				.views(20)
				.popularityScore(4.2)
				.build(),
			FeedReadResponseDto.builder()
				.feedId(3L)
				.title("Title 3")
				.content("Content 3")
				.category(Category.valueOf("PLACE"))
				.views(25)
				.popularityScore(4.5)
				.build()
		);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 조회 완료", responses));
	}
}
