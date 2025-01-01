package com.linkeleven.msa.area.domain.entity;

import java.util.List;

import com.linkeleven.msa.area.domain.vo.Address;
import com.linkeleven.msa.area.domain.vo.Coordinate;
import com.linkeleven.msa.area.domain.vo.PlaceName;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_location")
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

	@OneToMany(mappedBy = "location",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<AreaMatch> areaMatchList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;









}
