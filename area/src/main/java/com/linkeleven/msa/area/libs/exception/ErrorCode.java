package com.linkeleven.msa.area.libs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/*  400 BAD_REQUEST : 잘못된 요청  */
	ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),
	LATITUDE_OUT_OF_RANGE(400, "위도값은 33 이상 38.5 이하입니다."),
	LONGITUDE_OUT_OF_RANGE(400, "경도값은 124 이상 132 이하입니다."),
	MAPX_OUT_OF_RANGE(400, "지도의 X 픽셀값은 100000 이상 1000000 이하입니다."),
	MAPY_OUT_OF_RANGE(400, "지도의 Y 픽셀값은 1800000 이상 2500000  이하입니다."),
	PLACE_NAME_CANNOT_BE_NULL_OR_EMPTY(400, "장소 이름은 필수입니다."),
	PLACE_NAME_LENGTH_EXCEEDED(400, "장소 이름은 최대 50자까지 가능합니다."),
	ADDRESS_CANNOT_BE_NULL_OR_EMPTY(400, "주소값은 필수입니다."),
	ADDRESS_LENGTH_EXCEEDED(400, "주소는 최대 100자까지 가능합니다."),
	REGION_CANNOT_BE_NULL_OR_EMPTY(400, "지역 정보 입력은 필수입니다."),
	REGION_LENGTH_EXCEEDED(400, "지역 정보는 최대 50자까지 가능합니다."),
	ILLEGAL_MATCH_LOCATION(400, "잘못된 장소 매칭"),
	ILLEGAL_MATCH_CATEGORY(400, "잘못된 카테고리 매칭"),

	/*  401 UNAUTHORIZED : 인증 안됨  */
	UNAUTHORIZED(401, "인증되지 않았습니다."),

	/*  403 FORBIDDEN : 권한 없음  */
	FORBIDDEN(403, "권한이 없습니다."),


	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
	ACCESS_DENIED(404, "접근 권한이 없습니다."),
	NOT_FOUND_AREA(400, "찾으시는 지역 정보가 없습니다."),
	NOT_FOUND_ES_LOCATION(400, "엘라스틱 서치에 해당 장소 데이터가 없습니다."),
	/*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
	TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

	/*  409 CONFLICT : Resource 중복  */

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다.");


	/*  502 BAD_GATEWAY  연결 실패   */

	private final Integer httpStatus;
	private final String message;
}
