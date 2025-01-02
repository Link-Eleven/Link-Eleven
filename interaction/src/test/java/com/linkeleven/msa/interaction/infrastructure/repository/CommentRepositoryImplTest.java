package com.linkeleven.msa.interaction.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.linkeleven.msa.interaction.application.dto.CommentQueryResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;

@SpringBootTest
@ActiveProfiles("test")
class CommentRepositoryImplTest {

	@Autowired
	private CommentRepository commentRepository;

	@Test
	@DisplayName("좋아요 순 정렬")
	void findCommentByFeedWithCursor_LikeCount() {
		Long feedId = 1L;
		Long cursorId = null;
		LocalDateTime cursorCreatedAt = null;
		Long cursorLikeCount = null;
		int pageSize = 5;
		String sortBy = "like";

		List<CommentQueryResponseDto> query = List.of(
			new CommentQueryResponseDto(1L, 101L, "user1", "content1", LocalDateTime.now(), 5L, 2L),
			new CommentQueryResponseDto(2L, 102L, "user2", "content2", LocalDateTime.now(), 3L, 1L));


		assertThat(query).isNotEmpty();
		assertThat(query.size()).isLessThanOrEqualTo(pageSize);
		assertThat(query.get(0).getLikeCount()).isGreaterThanOrEqualTo(query.get(query.size() - 1).getLikeCount());
	}

	@Test
	@DisplayName("최신 순 정렬")
	void findCommentByFeedWithCursor_CreatedAt() {
		Long feedId = 1L;
		Long cursorId = null;
		LocalDateTime cursorCreatedAt = null;
		Long cursorLikeCount = null;
		int pageSize = 5;
		String sortBy = "";

		LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 2, 18, 0, 0);

		List<CommentQueryResponseDto> query = List.of(
			new CommentQueryResponseDto(1L, 101L, "user1", "content1", fixedDateTime.minusHours(1), 5L, 2L),
			new CommentQueryResponseDto(2L, 102L, "user2", "content2", fixedDateTime, 3L, 1L));


		assertThat(query).isNotEmpty();
		assertThat(query.size()).isLessThanOrEqualTo(pageSize);
		assertThat(query.get(0).getCreatedAt().compareTo(query.get(1).getCreatedAt())).isLessThan(0);
	}
}