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
public class Coordinate {
	private int mapX;

	private int mapY;

	private double latitude;

	private double longitude;

	public static Coordinate of(int mapX, int mapY) {
		mapPixelValidator(mapX, mapY);
		return Coordinate.builder()
			.mapX(mapX)
			.mapY(mapY)
			.build();
	}


	private static void validate(double latitude, double longitude) {
		if (latitude >= 33.0 && latitude <= 38.5) {
			throw new CustomException(ErrorCode.LATITUDE_OUT_OF_RANGE);
		}

		if (longitude >= 124.0 && longitude <= 132.0) {
			throw new CustomException(ErrorCode.LONGITUDE_OUT_OF_RANGE);
		}
	}

	private static void mapPixelValidator (int mapX, double mapY) {
		if (mapX < 900000 || mapX > 1500000) {
			throw new CustomException(ErrorCode.MAPX_OUT_OF_RANGE);
		}

		if (mapY < 1300000 || mapY > 2200000) {
			throw new CustomException(ErrorCode.MAPY_OUT_OF_RANGE);
		}
	}

}
