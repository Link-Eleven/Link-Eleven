package com.linkeleven.msa.recommendation.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepository {
	private final JpaRecommendationRepository jpaRecommendationRepository;

	// private final RedisRecommendationRepository redisRecommendationRepository;
	@Override
	public void save(Recommendation recommendation) {
		jpaRecommendationRepository.save(recommendation);
	}
}
