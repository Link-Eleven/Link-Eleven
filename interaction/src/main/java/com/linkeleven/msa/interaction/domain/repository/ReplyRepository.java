package com.linkeleven.msa.interaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.interaction.domain.model.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> , ReplyRepositoryCustom{
	boolean existsByIdAndContentDetails_userId(Long targetId, Long targetAuthorId);
}
