package com.linkeleven.msa.interaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.interaction.domain.model.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> , CommentRepositoryCustom {
	boolean existsByIdAndDeletedAtIsNull(Long commentId);

	boolean existsByIdAndContentDetails_userId(Long targetId, Long targetAuthorId);
}
