package com.linkeleven.msa.feed.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.feed.domain.model.TopFeed;

public interface TopFeedRepository extends JpaRepository<TopFeed, Long> {
	@Query("SELECT tf FROM TopFeed tf WHERE tf.backupDate > :backupDate")
	List<TopFeed> findFeedsAfterDate(@Param("backupDate") LocalDateTime backupDate);

}
