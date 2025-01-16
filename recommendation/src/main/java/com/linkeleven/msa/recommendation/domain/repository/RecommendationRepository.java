package com.linkeleven.msa.recommendation.domain.repository;

import java.util.List;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

public interface RecommendationRepository {
	Recommendation findByUserId(Long userId);

	void saveOrUpdate(Recommendation recommendation);

	List<Recommendation> findAll();
}
