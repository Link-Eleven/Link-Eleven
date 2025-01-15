package com.linkeleven.msa.recommendation.domain.repository;

import java.util.List;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

public interface RecommendationRepository {
	void saveOrUpdate(Recommendation recommendation);

	List<Recommendation> findAll();
}
