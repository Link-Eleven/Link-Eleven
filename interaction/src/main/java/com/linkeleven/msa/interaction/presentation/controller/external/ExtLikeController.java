package com.linkeleven.msa.interaction.presentation.controller.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.external.LikeCountResponseDto;
import com.linkeleven.msa.interaction.application.service.LikeQueryService;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/feeds/{feedId}/likes")
public class ExtLikeController {

	private final LikeQueryService likeQueryService;

	@GetMapping
	public LikeCountResponseDto getLikeCount(
		@PathVariable Long feedId
	) {
		return likeQueryService.getLikeCount(feedId, ContentType.FEED);
	}
}
