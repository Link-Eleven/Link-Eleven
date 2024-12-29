package com.linkeleven.msa.interaction.domain.model.entity;

import java.time.LocalDateTime;

import com.linkeleven.msa.interaction.domain.model.vo.ContentDetails;
import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_comment")
public class Comment extends BaseTime{

	@Id
	@Tsid
	private Long id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "content", column = @Column(name = "content", nullable = false)),
		@AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false, updatable = false))
	})
	private ContentDetails contentDetails;

	@Column(nullable = false, updatable = false)
	private Long feedId;

	// @Column
	// private boolean isReported;
	//
	// @Column
	// private String reportReason;

	@Builder
	private Comment(ContentDetails contentDetails, Long feedId) {
		this.contentDetails = contentDetails;
		this.feedId = feedId;
		// this.isReported = false;
		// this.reportReason = null;
	}

	public static Comment of(ContentDetails contentDetails, Long feedId) {
		return Comment.builder()
			.contentDetails(contentDetails)
			.feedId(feedId)
			.build();
	}

	public void updateComment(String newContent) {
		if (isDeleted()) {
			throw new CustomException(ErrorCode.COMMENT_ALREADY_DELETED);
		}
		/**
		 *   0. 위 검증로직 서비스로 이동하기
		 *   1. 헤더로 받은 userId 검증
		 *   2. 파라미터로 받은 feedId가 같은지 검증
		 */
		this.contentDetails = ContentDetails.of(newContent, this.contentDetails.getUserId());
	}

	public void deleteComment() {
		if (isDeleted()) {
			this.setDeletedAt(LocalDateTime.now());
			this.contentDetails = ContentDetails.of("삭제된 댓글입니다.", this.contentDetails.getUserId());
		} else {
			throw new CustomException(ErrorCode.COMMENT_ALREADY_DELETED);
		}
	}


	/**
	 *  신고처리 된 경우엔 isReported를 확인해서 "블라인드된 댓글입니다." 를 반환해주면 됨
	 */

	// public void reportComment(String reason) {
	// 	if (this.isReported) {
	// 		throw new CustomException(ErrorCode.COMMENT_ALREADY_REPORTED);
	// 	}
	// 	this.isReported = true;
	// 	this.reportReason = reason;
	// }
	//
	// public void rollbackReportComment() {
	// 	if (!this.isReported) {
	// 		throw new CustomException(ErrorCode.COMMENT_IS_NOT_REPORTED);
	// 	}
	// 	this.isReported = false;
	// 	this.reportReason = null;
	// }


	private boolean isDeleted() {
		return this.getDeletedAt() == null;
	}
}
