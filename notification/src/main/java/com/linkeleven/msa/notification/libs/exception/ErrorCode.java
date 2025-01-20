package com.linkeleven.msa.notification.libs.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/*  400 BAD_REQUEST : 잘못된 요청  */
	EXPIRED_TOKEN(400, "만료된 토큰입니다."),
	NOTIFICATION_TYPE_CANNOT_BE_NULL(400, "알림 타입은 null 일 수 없습니다."),
	INVALID_NOTIFICATION_TYPE(400, "유효하지 않은 알림 타입입니다." ),
	FIREBASE_INVALID_ARGUMENT(400, "잘못된 파이어베이스 요청 입니다." )

	/*  401 UNAUTHORIZED : 인증 안됨  */,
	UNAUTHORIZED(401, "인증되지 않았습니다."),
	FIREBASE_THIRD_PARTY_AUTH_ERROR(401, "파이어베이스 인증 실패.")

	/*  403 FORBIDDEN : 권한 없음  */,
	FORBIDDEN(403, "권한이 없습니다."),
	FIREBASE_SENDER_ID_MISMATCH(403, "등록된 토큰이 파이어베이스 프로젝트와 일치하지 않습니다.")

	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */,
	ACCESS_DENIED(404, "접근 권한이 없습니다."),
	USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
	FIREBASE_TOKEN_UNREGISTERED(404, "파이어베이스 토큰을 찾을 수 없습니다.")

	/*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */,
	TIMEOUT_ERROR(408, "응답시간을 초과하였습니다.")

	/*  409 CONFLICT : Resource 중복  */

	/*  429 TOO MANY REQUESTS : 너무 많은 요청  */,
	FIREBASE_QUOTA_EXCEEDED(429,"요청이 너무 많습니다.")

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */,
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),
	INTERRUPTED_ERROR(500, " Interrupted 에러 발생."),
	FIREBASE_ERROR(500, " 특정되지 않은 파이어베이스 에러")

	/*  503 SERVER_UNAVAILABLE  :  서버 불가능  */,
	SERVER_UNAVAILABLE(503, "서버에 연결할 수 없습니다.");



	private final Integer httpStatus;
	private final String message;
}
