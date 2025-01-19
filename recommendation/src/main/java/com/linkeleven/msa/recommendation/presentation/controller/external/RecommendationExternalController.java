package com.linkeleven.msa.recommendation.presentation.controller.external;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.recommendation.application.service.RecommendationService;
import com.linkeleven.msa.recommendation.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/recommendations")
public class RecommendationExternalController {
	private final RecommendationService recommendationService;

	@GetMapping("/keywords/{userId}")
	public ResponseEntity<SuccessResponseDto<List<String>>> getKeywords(@PathVariable("userId") Long userId) {
		List<String> keywords = recommendationService.getKeywordsForUser(userId);
		return ResponseEntity.ok(SuccessResponseDto.success("keywords 요청 성공", keywords));
	}
}
