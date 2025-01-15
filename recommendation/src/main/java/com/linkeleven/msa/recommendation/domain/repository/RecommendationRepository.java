package com.linkeleven.msa.recommendation.domain.repository;

import java.util.List;
import java.util.Optional;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

public interface RecommendationRepository {
	Optional<Recommendation> findByUserId(Long userId);

	void saveOrUpdate(Recommendation recommendation);

	List<Recommendation> findAll();
}
