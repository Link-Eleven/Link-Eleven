package com.linkeleven.msa.recommendation.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepository {
	private final JpaRecommendationRepository jpaRecommendationRepository;
	private final RedisRecommendationRepository redisRecommendationRepository;

	@Override
	public void save(Recommendation recommendation) {
		redisRecommendationRepository.save(recommendation);
		jpaRecommendationRepository.save(recommendation);
	}

	@Override
	public void saveOrUpdate(Recommendation recommendation) {
		// Redis 업데이트
		redisRecommendationRepository.update(recommendation);

		// RDBMS 업데이트
		Optional<Recommendation> existingRecommendation =
			jpaRecommendationRepository.findByUserId(recommendation.getUserId());

		if (existingRecommendation.isPresent()) {
			Recommendation existing = existingRecommendation.get();
			existing.updateKeywords(recommendation.getKeywords());
			jpaRecommendationRepository.save(existing);
		} else {
			jpaRecommendationRepository.save(recommendation);
		}
	}
}
