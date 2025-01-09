package com.linkeleven.msa.feed.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.feed.domain.model.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {

	@EntityGraph(attributePaths = "files")
	@Query("SELECT f from Feed f where f.feedId = :feedId AND f.deletedAt IS NULL")
	Optional<Feed> findByIdAndDeletedAt(Long feedId);

	boolean existsByFeedId(Long feedId);

	@Query("UPDATE Feed f SET f.views = f.views + 1 WHERE f.feedId = :feedId")
	@Modifying
	@Transactional
	void incrementViews(@Param("feedId") Long feedId);

	@Query("SELECT f FROM Feed f WHERE f.deletedAt IS NULL AND f.createdAt >= :cutoffDate ORDER BY f.popularityScore DESC")
	List<Feed> findTopFeeds(@Param("cutoffDate") LocalDateTime cutoffDate);

	boolean existsByIdAndUserId(Long feedId, Long userId);
}
