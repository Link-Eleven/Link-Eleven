package com.link_eleven.msa.log.domain.repository;

import java.util.List;

import com.link_eleven.msa.log.domain.model.FeedLog;

public interface FeedLogRepository {
	void save(FeedLog log);

	List<FeedLog> getLatestLogs(Long userId);
}
