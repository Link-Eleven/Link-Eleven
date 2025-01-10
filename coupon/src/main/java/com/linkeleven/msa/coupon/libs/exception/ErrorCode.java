package com.linkeleven.msa.coupon.libs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/*  400 BAD_REQUEST : 잘못된 요청  */
	COUPON_CANNOT_BE_ISSUED_YET(400, "현재는 쿠폰 발급을 할 수 없습니다."),
	INVALID_QUANTITY(400, "잘못된 수량이 제공되었습니다."),
	INVALID_POLICY_IDS(400, "유효하지 않은 정책 ID가 포함되어 있습니다."),
	INVALID_COUPON_CODE(400, "유효하지 않은 쿠폰 코드입니다."),

	/*  403 FORBIDDEN : 권한 없음  */
	FORBIDDEN(403, "권한이 없습니다."),

	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
	COUPON_NOT_FOUND(404, "쿠폰이 존재하지 않습니다."),
	COUPON_ALREADY_ISSUED(404, "이미 쿠폰이 발급되었습니다."),
	POLICY_NOT_FOUND(404, "정책을 찾을 수 없습니다"),
	NO_AVAILABLE_POLICY(404, "사용 가능한 정책이 없습니다"),

	/*  409 CONFLICT : Resource 중복  */
	DUPLICATE_FEED_ID(409, "중복된 쿠폰 피드 ID 입니다."),

	/* 410 Gone - 리소스가 더 이상 존재하지 않음 */
	EXPIRED_COUPON(410, "쿠폰이 만료되었습니다."),
	COUPON_SOLD_OUT(410, "쿠폰이 모두 소진되었습니다"),

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),
	REDIS_OPERATION_ERROR(500, "Redis 작업 중 오류가 발생했습니다"),

	// 503 Service Unavailable - 서비스 일시적 사용 불가
	LOCK_ACQUISITION_FAILED(503, "Lock을 획득하지 못했습니다");

	private final Integer httpStatus;
	private final String message;
}