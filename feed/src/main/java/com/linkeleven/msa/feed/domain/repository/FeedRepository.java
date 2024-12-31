package com.linkeleven.msa.feed.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.linkeleven.msa.feed.domain.model.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	@Query("SELECT f from Feed f where f.feedId = :feedId AND f.deletedAt IS NULL")
	Optional<Feed> findByIdAndDeletedAt(Long feedId);

}
