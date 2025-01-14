package com.linkeleven.msa.recommendation.domain.repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

public interface RecommendationRepository {

	void save(Recommendation recommendation);
}
