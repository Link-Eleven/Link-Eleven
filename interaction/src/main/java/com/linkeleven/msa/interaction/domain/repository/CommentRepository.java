package com.linkeleven.msa.interaction.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.interaction.domain.model.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> , CommentRepositoryCustom {
	boolean existsByIdAndDeletedAtIsNull(Long commentId);

	boolean existsByIdAndContentDetailsUserId(Long targetId, Long targetAuthorId);

	@Query("SELECT c.feedId, COUNT(c) FROM Comment c " +
	"WHERE c.feedId IN :feedIdList GROUP BY c.feedId")
	List<Object[]> countByFeedIdList(@Param("feedIdList")List<Long> feedIdList);
}
