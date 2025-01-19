package com.linkeleven.msa.recommendation.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

public interface JpaRecommendationRepository extends JpaRepository<Recommendation, Long> {
	Recommendation findByUserId(Long userId);
}
