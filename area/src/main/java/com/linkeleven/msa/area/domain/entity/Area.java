package com.linkeleven.msa.area.domain.entity;

import com.linkeleven.msa.area.domain.vo.Region;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "p_area")
public class Area {
	@Id
	@Tsid
	private Long id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "region_code", column = @Column(name = "region_code", nullable = false)),
		@AttributeOverride(name = "region_name", column = @Column(name = "region_name", nullable = false)),
		@AttributeOverride(name = "city_code", column = @Column(name = "city_code", nullable = false)),
		@AttributeOverride(name = "city_name", column = @Column(name = "city_name", nullable = false)),
		@AttributeOverride(name = "subregion_code", column = @Column(name = "subregion_code", nullable = false)),
		@AttributeOverride(name = "subregion_name", column = @Column(name = "subregion_name", nullable = false)),
	})
	private Region region;



}
