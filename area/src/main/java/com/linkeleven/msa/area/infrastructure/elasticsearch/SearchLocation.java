package com.linkeleven.msa.area.infrastructure.elasticsearch;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.linkeleven.msa.area.domain.entity.Location;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(indexName = "location")
public class SearchLocation {
	@Id
	private String id;

	@Field(name = "location_id", type = FieldType.Long)
	private Long locationId;

	@Field(name = "area_id", type = FieldType.Long)
	private Long areaId;

	@Field(name = "keyword_set", type = FieldType.Keyword)
	private Set<String> keywordSet;

	@Field(name = "map_x", type = FieldType.Integer)
	private int mapX;

	@Field(name = "map_y", type = FieldType.Integer)
	private int mapY;

	@Field(name = "place_name", type = FieldType.Text)
	private String placeName;

	@Field(name = "address", type = FieldType.Text)
	private String address;

	@Field(name = "category", type = FieldType.Keyword)
	private String category;


	public static SearchLocation of(Location location, String keyword){
		Set<String> initialKeywords = new HashSet<>();
		initialKeywords.add(keyword);
		return SearchLocation.builder()
			.locationId(location.getId())
			.areaId(location.getArea().getId())
			.keywordSet(initialKeywords)
			.mapX(location.getCoordinate().getMapX())
			.mapY(location.getCoordinate().getMapY())
			.placeName(location.getPlaceName().toString())
			.category(location.getCategory().getCategoryType().toString())
			.build();
	}

	public void addKeyword(String keyword) {
		if (this.keywordSet == null) {
			this.keywordSet = new HashSet<>();
		}
		this.keywordSet.add(keyword);
	}

}
