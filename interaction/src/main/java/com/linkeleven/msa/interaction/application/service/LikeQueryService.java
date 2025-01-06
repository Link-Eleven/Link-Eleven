package com.linkeleven.msa.interaction.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.application.dto.external.LikeCountResponseDto;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.domain.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeQueryService {

	private final LikeRepository likeRepository;

	public LikeCountResponseDto getLikeCount(Long feedId, ContentType type) {
		Long count = likeRepository.countByTarget_TargetIdAndTarget_ContentType(feedId, type)
			.orElseGet(() -> 0L);

		return new LikeCountResponseDto(count);
	}
}
