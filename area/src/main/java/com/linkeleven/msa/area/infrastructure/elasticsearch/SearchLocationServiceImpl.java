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

import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.application.service.SearchLocationService;
import com.linkeleven.msa.area.domain.common.CategoryType;
import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.infrastructure.repository.LocationElasticSearchRepository;

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

		System.out.println("Generated Query: " + query.getQuery());

		SearchHits<SearchLocation> searchHits = elasticsearchOperations.search(query, SearchLocation.class);

		return searchHits.getSearchHits().stream()
			.map(hit -> toDto(hit.getContent()))
			.collect(Collectors.toList());
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
		// List<Long>을 FieldValue 리스트로 변환
		List<FieldValue> fieldValues = locationIdList.stream()
			.map(FieldValue::of) // FieldValue.of를 사용하여 변환
			.toList();

		// TermsQueryField에 변환된 FieldValue 리스트 사용
		TermsQueryField termsQueryField = new TermsQueryField.Builder()
			.value(fieldValues)
			.build();

		// NativeQuery 생성
		NativeQuery query = NativeQuery.builder()
			.withQuery(QueryBuilders.terms(t -> t.field("location_id").terms(termsQueryField)))
			.build();

		SearchHits<SearchLocation> searchHits = elasticsearchOperations.search(query, SearchLocation.class);

		return searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toMap(SearchLocation::getLocationId, doc -> doc));
	}

	private static LocationSearchResponseDto toDto(SearchLocation searchLocation) {
		return LocationSearchResponseDto.builder()
			.searchLocationId(searchLocation.getId())
			.locationId(searchLocation.getLocationId())
			.areaId(searchLocation.getAreaId())
			.mapX(searchLocation.getMapX())
			.mapY(searchLocation.getMapY())
			.placeName(searchLocation.getPlaceName())
			.address(searchLocation.getAddress())
			.category(searchLocation.getCategory())
			.build();
	}
}



