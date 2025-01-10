package com.linkeleven.msa.interaction.libs.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/*  400 BAD_REQUEST : 잘못된 요청  */
	EXPIRED_TOKEN(400, "만료된 토큰입니다."),
	CONTENT_CANNOT_BE_NULL_OR_EMPTY(400, "댓글 내용은 null 또는 빈 값일 수 없습니다."),
	USERNAME_CANNOT_BE_NULL_OR_EMPTY(400, "유저네임은 null 또는 빈 값일 수 없습니다."),
	CONTENT_TYPE_CANNOT_BE_NULL(400, "컨텐츠 타입은 null일 수 없습니다."),
	CONTENT_TOO_LONG(400, "100자 이내로 작성해야 합니다."),
	INVALID_USERID(400, "유효하지않은 유저ID입니다."),
	INVALID_TARGET_ID(400, "유효하지않은 타겟ID입니다."),
	INVALID_FEED_FOR_COMMENT(400, "유효하지않은 게시글입니다."),
	INVALID_CONTENT_TYPE(400, "유효하지않은 컨텐츠 타입입니다."),
	INVALID_COMMENT_FOR_REPLY(400, "유효하지않은 댓글입니다."),
	COMMENT_ALREADY_DELETED(400, "이미 삭제된 댓글입니다."),
	COMMENT_ALREADY_REPORTED(400, "이미 신고 처리된 댓글입니다."),
	COMMENT_IS_NOT_REPORTED(400, "신고된 댓글이 아닙니다."),
	REPLY_ALREADY_DELETED(400, "이미 삭제된 대댓글입니다."),
	REPLY_ALREADY_REPORTED(400, "이미 신고 처리된 대댓글입니다."),
	REPLY_IS_NOT_REPORTED(400, "신고된 대댓글이 아닙니다."),
	USER_IS_NOT_COMMENT_OWNER(400, "댓글 작성자가 아닙니다."),
	USER_IS_NOT_REPLY_OWNER(400, "대댓글 작성자가 아닙니다."),
	ALREADY_LIKE(400, "이미 좋아요를 누른 컨텐츠입니다."),

	/*  401 UNAUTHORIZED : 인증 안됨  */
	UNAUTHORIZED(401, "인증되지 않았습니다."),

	/*  403 FORBIDDEN : 권한 없음  */
	FORBIDDEN(403, "권한이 없습니다."),

	/*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
	ACCESS_DENIED(404, "접근 권한이 없습니다."),
	USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
	FEED_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
	COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다."),
	REPLY_NOT_FOUND(404, "대댓글을 찾을 수 없습니다."),
	TARGET_NOT_FOUND(404, "좋아요 대상을 찾을 수 없습니다."),
	LIKE_NOT_FOUND(404, "좋아요를 찾을 수 없습니다."),

	/*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
	TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

	/*  409 CONFLICT : Resource 중복  */

	/*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
	INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),
	INTERNAL_SERVER_ERROR_TO_SERIALIZE(500, "내부 서버 에러. 직렬화 실패"),
	INTERNAL_SERVER_ERROR_TO_DESERIALIZE(500, "내부 서버 에러. 역직렬화 실패"),
	INTERRUPTED_ERROR(500, " Interrupted 에러 발생."),

	/*  502 BAD_GATEWAY  연결 실패   */
	;

	private final Integer httpStatus;
	private final String message;
}
