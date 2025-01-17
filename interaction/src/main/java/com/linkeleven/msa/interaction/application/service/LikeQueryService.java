package com.linkeleven.msa.interaction.application.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.interaction.application.dto.external.LikeCountResponseDto;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.domain.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeQueryService {

	private final LikeRepository likeRepository;

	@Transactional(readOnly = true)
	public LikeCountResponseDto getLikeCount(List<Long> feedIdList, ContentType type) {
		List<Object[]> resultList = likeRepository.countByTargetIdListAndTargetContentType(feedIdList, type);
		Map<Long, Integer> likeCounts = new HashMap<>();

		for (Long feedId : feedIdList) {
			likeCounts.put(feedId, 0);
		}

		for (Object[] likeList : resultList) {
			Long feedId = (Long)likeList[0];
			Integer count = ((Long)likeList[1]).intValue();
			likeCounts.put(feedId, count);
		}

		return new LikeCountResponseDto(likeCounts);
	}
}
