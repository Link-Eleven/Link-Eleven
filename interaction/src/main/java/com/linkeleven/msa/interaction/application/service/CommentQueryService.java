package com.linkeleven.msa.interaction.application.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.interaction.application.dto.CommentQueryResponseDto;
import com.linkeleven.msa.interaction.application.dto.external.CommentCountResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

	private final CommentRepository commentRepository;

	@Transactional(readOnly = true)
	public Slice<CommentQueryResponseDto> getCommentsWithCursor(
		Long feedId, Long cursorId, LocalDateTime cursorCreatedAt, Long cursorLikeCount,
		int pageSize, String sortByEnum)
	{
		return commentRepository.findCommentByFeedWithCursor(feedId, cursorId, pageSize, sortByEnum,
			cursorLikeCount, cursorCreatedAt);
	}

	@Transactional(readOnly = true)
	public CommentCountResponseDto getCommentCount(List<Long> feedIdList) {
		List<Object[]> resultList = commentRepository.countByFeedIdList(feedIdList);
		Map<Long, Integer> commentCounts = new HashMap<>();

		for (Long feedId : feedIdList) {
			commentCounts.put(feedId, 0);
		}

		for (Object[] commentList : resultList) {
			Long feedId = (Long)commentList[0];
			Integer count = ((Long)commentList[1]).intValue();
			commentCounts.put(feedId, count);
		}

		return new CommentCountResponseDto(commentCounts);
	}
}
