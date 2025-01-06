package com.linkeleven.msa.interaction.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.interaction.domain.model.entity.Like;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;

public interface LikeRepository extends JpaRepository<Like, Long> {
	boolean existsByTarget_TargetIdAndUserId(Long targetId, Long userId);

	Optional<Like> findByTarget_TargetIdAndUserId(Long targetId, Long userId);

	Optional<Long> countByTarget_TargetIdAndTarget_ContentType(Long feedId, ContentType type);
}
