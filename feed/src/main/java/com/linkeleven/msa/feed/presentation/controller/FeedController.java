package com.linkeleven.msa.feed.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.feed.presentation.request.FeedRequestDto;

@RestController
@RequestMapping("/api/feeds")
public class FeedController {

	@PostMapping
	public ResponseEntity<SuccessResponseDto<FeedCreateResponseDto>> createFeed(
		@RequestBody FeedRequestDto feedRequestDto) {
		// TODO : 게시글 생성 실제 구현
		// 더미 데이터 생성 로직
		FeedCreateResponseDto response = FeedCreateResponseDto.builder()
			.feedId(1L)
			.title(feedRequestDto.getTitle())
			.content(feedRequestDto.getContent())
			.category(feedRequestDto.getCategory())
			.views(feedRequestDto.getViews())
			.popularityScore(feedRequestDto.getPopularityScore())
			.build();
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 생성 성공", response));
	}

	@GetMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<FeedReadResponseDto>> getDetailFeed(@PathVariable Long feedId) {
		// TODO : 게시글 단건 조회 실제 구현
		// 더미 데이터 조회 로직
		FeedReadResponseDto response = FeedReadResponseDto.builder()
			.feedId(feedId)
			.areaId(1L)
			.userId(123L)
			.title("Sample Title")
			.content("Sample Content")
			.category("PLACE")
			.views(10)
			.popularityScore(4.5)
			.build();
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 단건 조회", response));
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
				.category("HOTEL")
				.views(15)
				.popularityScore(4.0)
				.build(),
			FeedReadResponseDto.builder()
				.feedId(2L)
				.title("Title 2")
				.content("Content 2")
				.category("RESTAURANT")
				.views(20)
				.popularityScore(4.2)
				.build(),
			FeedReadResponseDto.builder()
				.feedId(3L)
				.title("Title 3")
				.content("Content 3")
				.category("PLACE")
				.views(25)
				.popularityScore(4.5)
				.build()
		);
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 조회 완료", responses));
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<FeedUpdateResponseDto>> updateFeed(@PathVariable Long feedId,
		@RequestBody FeedRequestDto feedRequestDto) {
		// TODO : 게시글 수정 실제 구현
		// 더미 데이터 업데이트 로직
		FeedUpdateResponseDto response = FeedUpdateResponseDto.builder()
			.feedId(feedId)
			.userId(feedRequestDto.getUserId())
			.title(feedRequestDto.getTitle())
			.content(feedRequestDto.getContent())
			.category(feedRequestDto.getCategory())
			.views(feedRequestDto.getViews())
			.popularityScore(feedRequestDto.getPopularityScore())
			.build();
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 수정 완료", response));
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteFeed(@PathVariable Long feedId) {
		// TODO : 게시글 삭제 실제 구현
		return ResponseEntity.ok(SuccessResponseDto.success("게시글 삭제 완료", null));
	}
}
