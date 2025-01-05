package com.linkeleven.msa.interaction.domain.model.entity;

import java.time.LocalDateTime;

import com.linkeleven.msa.interaction.domain.model.vo.ContentDetails;

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
		@AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false, updatable = false)),
		@AttributeOverride(name = "username", column = @Column(name = "username", nullable = false))
	})
	private ContentDetails contentDetails;

	@Column(nullable = false, updatable = false)
	private Long feedId;

	// @Column
	// private boolean isReported;
	//
	// @Column
	// private String reportReason;

	@Builder(access = AccessLevel.PRIVATE)
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
		this.contentDetails = ContentDetails.of(newContent, this.contentDetails.getUserId(), this.contentDetails.getUsername());
	}

	public void deleteComment() {
			this.setDeletedAt(LocalDateTime.now());
			this.contentDetails = ContentDetails.of("삭제된 댓글입니다.", this.contentDetails.getUserId(), this.contentDetails.getUsername());
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
}
