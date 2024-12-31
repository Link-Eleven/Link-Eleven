package com.linkeleven.msa.feed.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.feed.domain.model.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
