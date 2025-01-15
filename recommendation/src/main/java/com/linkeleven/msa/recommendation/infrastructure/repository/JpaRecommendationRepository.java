package com.linkeleven.msa.recommendation.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

public interface JpaRecommendationRepository extends JpaRepository<Recommendation, Long> {
	Optional<Recommendation> findByUserId(Long userId);
}
