package com.linkeleven.msa.area.infrastructure.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.linkeleven.msa.area.infrastructure.elasticsearch.SearchLocation;

public interface LocationElasticSearchRepository extends ElasticsearchRepository<SearchLocation, Long> {
}
