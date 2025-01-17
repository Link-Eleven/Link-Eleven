package com.linkeleven.msa.interaction.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.interaction.domain.model.entity.Like;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;

public interface LikeRepository extends JpaRepository<Like, Long> {
	boolean existsByTarget_TargetIdAndUserId(Long targetId, Long userId);

	Optional<Like> findByTarget_TargetIdAndUserId(Long targetId, Long userId);

	@Query("SELECT l.target.targetId, COUNT(l) FROM Like l " +
	"WHERE l.target.targetId IN :feedIdList AND l.target.contentType = :type " +
	"GROUP BY l.target.targetId")
	List<Object[]> countByTargetIdListAndTargetContentType(
		@Param("feedIdList") List<Long> feedIdList,
		@Param("type") ContentType type);

}
