package com.linkeleven.msa.recommendation.domain.repository;

import java.util.List;

import com.linkeleven.msa.recommendation.domain.model.FeedLog;

public interface FeedLogRepository {
	void save(FeedLog log);

	List<FeedLog> getLatestLogs(Long userId);
}
