package com.linkeleven.msa.area.infrastructure.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.area.application.dto.LocationSearchDetailResponseDto;
import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.application.service.SearchLocationService;
import com.linkeleven.msa.area.domain.common.CategoryType;
import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.infrastructure.repository.LocationElasticSearchRepository;
import com.linkeleven.msa.area.libs.exception.CustomException;
import com.linkeleven.msa.area.libs.exception.ErrorCode;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchLocationServiceImpl implements SearchLocationService {
	private final ElasticsearchOperations elasticsearchOperations;
	private final LocationElasticSearchRepository locationElasticSearchRepository;

	public SearchLocationServiceImpl(
		ElasticsearchOperations elasticsearchOperations,
		LocationElasticSearchRepository locationElasticSearchRepository
	) {
		this.elasticsearchOperations = elasticsearchOperations;
		this.locationElasticSearchRepository = locationElasticSearchRepository;
	}

	@Override
	public List<LocationSearchResponseDto> searchKeyword(String keyword, CategoryType categoryType, Long areaId) {
		BoolQuery.Builder boolQuery = QueryBuilders.bool();

		boolQuery.must(QueryBuilders.match(t -> t.field("keyword_set").query(keyword)));

		boolQuery.filter(QueryBuilders.term(t -> t.field("category").value(categoryType.toString())));

		boolQuery.filter(QueryBuilders.term(t -> t.field("area_id").value(areaId)));

		NativeQuery query = NativeQuery.builder()
			.withQuery(boolQuery.build()._toQuery())
			.build();

		log.info("Generated Query: " + query.getQuery());

		SearchHits<SearchLocation> searchHits = elasticsearchOperations.search(query, SearchLocation.class);

		return searchHits.getSearchHits().stream()
			.map(hit -> LocationSearchResponseDto.from(hit.getContent()))
			.toList();
	}

	@Override
	public LocationSearchDetailResponseDto searchLocation(Long locationId) {
		BoolQuery.Builder boolQuery = QueryBuilders.bool();
		boolQuery.filter(QueryBuilders.term(t -> t.field("location_id").value(locationId)));

		NativeQuery query = NativeQuery.builder()
			.withQuery(boolQuery.build()._toQuery())
			.build();

		log.info("Generated Query: {}", query.getQuery());

		SearchHits<SearchLocation> searchHits = elasticsearchOperations.search(query, SearchLocation.class);

		if (!searchHits.hasSearchHits()) {
			throw new CustomException(ErrorCode.NOT_FOUND_ES_LOCATION);
		}

		SearchLocation location = searchHits.getSearchHit(0).getContent();

		return LocationSearchDetailResponseDto.from(location);
	}



	@Override
	public void create(List<Location> locationList, String keyword) {
		List<Long> locationIdList = locationList.stream()
			.map(Location::getId)
			.toList();

		Map<Long, SearchLocation> existDocs = findExistingDocs(locationIdList);

		List<SearchLocation> searchLocationList = new ArrayList<>();

		for (Location location : locationList) {
			SearchLocation existingDoc = existDocs.get(location.getId());

			if (existingDoc != null) {
				existingDoc.addKeyword(keyword);
				searchLocationList.add(existingDoc);
			} else {
				searchLocationList.add(SearchLocation.of(location, keyword));
			}
		}

		locationElasticSearchRepository.saveAll(searchLocationList);
	}



	// 기존에 있는 location 데이터 있는지 확인
	private Map<Long, SearchLocation> findExistingDocs(List<Long> locationIdList) {
		List<FieldValue> fieldValues = locationIdList.stream()
			.map(FieldValue::of)
			.toList();

		TermsQueryField termsQueryField = new TermsQueryField.Builder()
			.value(fieldValues)
			.build();


		NativeQuery query = NativeQuery.builder()
			.withQuery(QueryBuilders.terms(t -> t.field("location_id").terms(termsQueryField)))
			.build();

		SearchHits<SearchLocation> searchHits = elasticsearchOperations.search(query, SearchLocation.class);

		return searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toMap(SearchLocation::getLocationId, doc -> doc));
	}

}



