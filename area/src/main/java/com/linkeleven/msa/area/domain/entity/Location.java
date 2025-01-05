package com.linkeleven.msa.area.domain.entity;

import com.linkeleven.msa.area.domain.vo.Address;
import com.linkeleven.msa.area.domain.vo.Coordinate;
import com.linkeleven.msa.area.domain.vo.PlaceName;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_location")
@Slf4j
public class Location extends BaseTime{

	@Id
	@Tsid
	private Long id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "map_x", column = @Column(name = "map_x", nullable = false)),
		@AttributeOverride(name = "map_y", column = @Column(name = "map_y", nullable = false)),
		@AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
		@AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
	})
	private Coordinate coordinate;

	@Embedded
	@AttributeOverrides(
		@AttributeOverride(name = "place_name", column = @Column(name = "place_name", nullable = false))
	)
	private PlaceName placeName;

	@Embedded
	@AttributeOverrides(
		@AttributeOverride(name = "address", column = @Column(name = "address", nullable = false))
	)
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name =  "area_id", nullable = false)
	private Area area;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	public static Location createLocation(
		Coordinate coordinate,
		PlaceName placeName,
		Address address
	){

		return Location.builder()
			.coordinate(coordinate)
			.placeName(placeName)
			.address(address)
			.build();
	}

	public void updateCategory(Category category){
		log.info("Updating category for Location {} with Category {}", this.id, category.getId());
		this.category = category;
	}

	public void updateArea(Area area){
		log.info("Updating area for Location {} with Area {}", this.id, area.getId());
		this.area = area;
	}







}
