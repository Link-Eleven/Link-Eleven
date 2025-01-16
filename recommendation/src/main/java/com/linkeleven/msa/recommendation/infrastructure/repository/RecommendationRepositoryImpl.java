package com.linkeleven.msa.recommendation.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.RecommendationRepository;
import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepository {
	private final JpaRecommendationRepository jpaRecommendationRepository;
	private final RedisRecommendationRepository redisRecommendationRepository;

	@Override
	public Recommendation findByUserId(Long userId) {
		RecommendationCache cachedData = redisRecommendationRepository.findByUserId(userId);
		if (cachedData != null) {
			return cachedData.toEntity();
		}

		Recommendation dbData = jpaRecommendationRepository.findByUserId(userId);
		if (dbData != null) {
			redisRecommendationRepository.save(dbData);
		}
		return dbData;
	}

	@Override
	public void saveOrUpdate(Recommendation recommendation) {
		redisRecommendationRepository.update(recommendation);

		Recommendation existingRecommendation = jpaRecommendationRepository.findByUserId(recommendation.getUserId());
		if (existingRecommendation != null) {
			existingRecommendation.updateKeywords(recommendation.getKeywords());
			jpaRecommendationRepository.save(existingRecommendation);
		} else {
			jpaRecommendationRepository.save(recommendation);
		}
	}

	@Override
	public List<Recommendation> findAll() {
		return jpaRecommendationRepository.findAll();
	}
}