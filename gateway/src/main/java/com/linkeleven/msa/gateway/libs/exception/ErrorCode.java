package com.linkeleven.msa.gateway.libs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/*  400 BAD_REQUEST : 잘못된 요청  */
	ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),


	/*  401 UNAUTHORIZED : 인증 안됨  */
	UNAUTHORIZED(401, "인증되지 않았습니다."),
	INVALID_JWT_SIGNATURE(401, "유효하지 않는 JWT 서명입니다."),
	EXPIRED_JWT_TOKEN(401, "만료된 JWT 토큰입니다."),
	UNSUPPORTED_JWT_TOKEN(401, "지원되지 않는 JWT 토큰입니다."),
	INVALID_JWT_CLAIMS(401, "JWT claims가 유효하지 않습니다."),

	/*  403 FORBIDDEN : 권한 없음  */
	FORBIDDEN(403, "권한이 없습니다."),


	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
	ACCESS_DENIED(404, "접근 권한이 없습니다."),


	/*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
	TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

	/*  409 CONFLICT : Resource 중복  */
	USERNAME_ALREADY_EXISTS(409, "이미 존재하는 사용자 이름입니다."),

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다.");


	/*  502 BAD_GATEWAY  연결 실패   */

	private final Integer httpStatus;
	private final String message;
}