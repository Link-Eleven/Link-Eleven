package com.linkeleven.msa.area.domain.vo;

import com.linkeleven.msa.area.libs.exception.CustomException;
import com.linkeleven.msa.area.libs.exception.ErrorCode;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Region {
	private String regionCode;
	private String regionName;
	private String cityCode;
	private String cityName;
	private String subregionCode;
	private String subregionName;


	public static Region of(
		String regionCode,
		String regionName,
		String cityCode,
		String cityName,
		String subregionCode,
		String subregionName
	) {
		validate(regionCode, regionName, cityCode, cityName, subregionCode, subregionCode);
		return Region.builder()
			.regionCode(regionCode)
			.regionName(regionName)
			.cityCode(cityCode)
			.cityName(cityName)
			.subregionCode(subregionCode)
			.subregionCode(subregionName)
			.build();
	}

	private static void validate(
		String regionCode,
		String regionName,
		String cityCode,
		String cityName,
		String subregionCode,
		String subregionName
	) {
		if (regionCode == null || regionCode.trim().isEmpty()
			&& regionName == null || regionName.trim().isEmpty()
			&& cityCode == null || cityCode.trim().isEmpty()
			&& cityName == null || cityName.trim().isEmpty()
			&& subregionCode == null || subregionCode.trim().isEmpty()
			&& subregionName == null || subregionName.trim().isEmpty()
		) {
			throw new CustomException(ErrorCode.REGION_CANNOT_BE_NULL_OR_EMPTY);
		}
		if (regionCode.length() > 50
			&& regionName.length() > 50
			&& cityCode.length() > 50
			&& cityName.length() > 50
			&& subregionCode.length() > 50
			&& subregionName.length() > 50
		) {
			throw new CustomException(ErrorCode.REGION_LENGTH_EXCEEDED);
		}
	}
}
