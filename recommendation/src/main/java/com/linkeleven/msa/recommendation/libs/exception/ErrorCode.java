package com.linkeleven.msa.recommendation.libs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/*  400 BAD_REQUEST : 잘못된 요청  */
	ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),

	/*  401 UNAUTHORIZED : 인증 안됨  */
	UNAUTHORIZED(401, "인증되지 않았습니다."),

	/*  403 FORBIDDEN : 권한 없음  */
	FORBIDDEN(403, "권한이 없습니다."),

	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
	ACCESS_DENIED(404, "접근 권한이 없습니다."),
	RECOMMENDATION_NOT_FOUND(404, "추천 키워드를 찾을 수 없습니다."),
	/*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
	TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

	/*  409 CONFLICT : Resource 중복  */

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
	FEED_LOG_SAVE_ERROR(500, "피드 로그 저장에 실패했습니다."),
	FEED_LOG_READ_ERROR(500, "피드 로그를 가져오지 못했습니다."),
	FEED_LOG_PARSE_ERROR(500, "피드 로그 분석에 실패했습니다."),
	KAFKA_SEND_FAILURE(500, "Kafka에 메시지를 보내지 못했습니다."),
	KAFKA_CONSUME_FAILURE(500, "메시지 수신 중 오류가 발생하였습니다."),
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),
	REDIS_CONNECTION_ERROR(503, "Redis 서버 연결에 실패했습니다."),
	REDIS_SYNC_ERROR(500, "Redis 동기화 중 오류가 발생했습니다."),
	REDIS_OPERATION_ERROR(500, "Redis 작업 중 오류가 발생했습니다."),
	REDIS_MAX_RETRY_EXCEEDED(503, "Redis 작업 최대 재시도 횟수를 초과했습니다.");;


	/*  502 BAD_GATEWAY  연결 실패   */

	private final Integer httpStatus;
	private final String message;
}
