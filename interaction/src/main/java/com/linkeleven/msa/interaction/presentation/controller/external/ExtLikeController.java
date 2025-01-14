package com.linkeleven.msa.interaction.presentation.controller.external;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.external.LikeCountResponseDto;
import com.linkeleven.msa.interaction.application.service.LikeQueryService;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExtLikeController {

	private final LikeQueryService likeQueryService;

	@GetMapping("/external/feeds/likes")
	public LikeCountResponseDto getLikeCount(
		@RequestParam("feedIdList") List<Long> feedIdList
	) {
		return likeQueryService.getLikeCount(feedIdList, ContentType.FEED);
	}
}
