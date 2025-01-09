package com.linkeleven.msa.coupon.libs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	COUPON_SOLD_OUT(204, "해당 쿠폰이 모두 소진되었습니다."),

	/*  400 BAD_REQUEST : 잘못된 요청  */
	ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),
	COUPON_CANNOT_BE_ISSUED_YET(400, "현재는 쿠폰 발급을 할 수 없습니다."),
	/*  401 UNAUTHORIZED : 인증 안됨  */
	UNAUTHORIZED(401, "인증되지 않았습니다."),

	/*  403 FORBIDDEN : 권한 없음  */
	FORBIDDEN(403, "권한이 없습니다."),

	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
	ACCESS_DENIED(404, "접근 권한이 없습니다."),
	COUPON_NOT_FOUND(404, "쿠폰이 존재하지 않습니다."),
	NO_AVAILABLE_POLICY(404, "유효한 쿠폰 정책이 없습니다."),
	NO_AVAILABLE_COUPON(404, "유효한 쿠폰이 없습니다."),
	COUPON_ALREADY_ISSUED(404, "이미 쿠폰이 발급되었습니다."),
	/*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
	TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

	/*  409 CONFLICT : Resource 중복  */
	DUPLICATE_FEED_ID(409, "중복된 쿠폰 피드 ID 입니다."),

	EXPIRED_COUPON(410, "쿠폰이 만료되었습니다."),

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다.");


	/*  502 BAD_GATEWAY  연결 실패   */

	private final Integer httpStatus;
	private final String message;
}
