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
	private String code;
	private String sido;
	private String sigungu;
	private String eupmyeondong;
	private String ri;


	public static Region of(
		String code,
		String sido,
		String sigungu,
		String eupmyeondong,
		String ri
	) {
		validate(code, sido, sigungu, eupmyeondong, ri);
		return Region.builder()
			.code(code)
			.sido(sido)
			.sigungu(sigungu)
			.eupmyeondong(eupmyeondong)
			.ri(ri)
			.build();
	}

	private static void validate(
		String code,
		String sido,
		String sigungu,
		String eupmyeondong,
		String ri
	) {
		if (code == null || code.trim().isEmpty()
			&& sido == null || sido.trim().isEmpty()
			&& sigungu == null || sigungu.trim().isEmpty()
			&& eupmyeondong == null || eupmyeondong.trim().isEmpty()
			&& ri == null || ri.trim().isEmpty()
		) {
			throw new CustomException(ErrorCode.REGION_CANNOT_BE_NULL_OR_EMPTY);
		}
		if (code.length() > 50
			&& sido.length() > 50
			&& sigungu.length() > 50
			&& eupmyeondong.length() > 50
			&& ri.length() > 50
		) {
			throw new CustomException(ErrorCode.REGION_LENGTH_EXCEEDED);
		}
	}
}
