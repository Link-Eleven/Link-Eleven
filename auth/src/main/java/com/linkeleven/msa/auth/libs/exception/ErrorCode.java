package com.linkeleven.msa.auth.libs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/*  400 BAD_REQUEST : 잘못된 요청  */
	ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),
	CANNOT_FOLLOWING_SELF(400,"유저 본인을 팔로잉 할 수 없습니다."),


	/*  401 UNAUTHORIZED : 인증 안됨  */
	UNAUTHORIZED(401, "인증되지 않았습니다."),
	INVALID_PASSWORD(401, "비밀번호가 틀렸습니다."),

	/*  403 FORBIDDEN : 권한 없음  */
	FORBIDDEN(403, "권한이 없습니다."),
	USER_SELF_ACCESS_ONLY(403,"본인 정보만 가능합니다."),
	USER_ROLE_NOT_EQUEALS(403,"유저 권한이 일치하지 않습니다."),
	ONLY_COMPANY_USER(403,"관련없는 권한입니다."),


	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
	ACCESS_DENIED(404, "접근 권한이 없습니다."),
	USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
	NOT_FOLLOWING_USER(404,"팔로잉 하지 않은 유저입니다."),

	/*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
	TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

	/*  409 CONFLICT : Resource 중복  */
	USERNAME_ALREADY_EXISTS(409, "이미 존재하는 사용자 이름입니다."),
	ALREADY_FOLLOWING(409,"이미 팔로잉한 유저입니다."),
	ALREADY_UNFOLLOWING(409,"이미 팔로잉을 삭제한 유저입니다."),

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다.");


	/*  502 BAD_GATEWAY  연결 실패   */

	private final Integer httpStatus;
	private final String message;
}
