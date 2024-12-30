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
@Table(name = "p_reply")
public class Reply extends BaseTime{

	@Id
	@Tsid
	private Long id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "content", column = @Column(name = "content", nullable = false)),
		@AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false, updatable = false))
	})
	private ContentDetails contentDetails;

	@Column(name = "comment_id", nullable = false, updatable = false)
	private Long commentId;

	// @Column
	// private boolean isReported;
	//
	// @Column
	// private String reportReason;

	@Builder
	private Reply(ContentDetails contentDetails, Long commentId) {
		this.contentDetails = contentDetails;
		this.commentId = commentId;
		// this.isReported = false;
		// this.reportReason = null;
	}

	public static Reply of(ContentDetails contentDetails, Long commentId) {
		return Reply.builder()
			.contentDetails(contentDetails)
			.commentId(commentId)
			.build();
	}

	public void updateReply(String newContent) {
		this.contentDetails = ContentDetails.of(newContent, this.contentDetails.getUserId());
	}

	public void deleteReply() {
		if (isDeleted()) {
			this.setDeletedAt(LocalDateTime.now());
		} else {
			throw new CustomException(ErrorCode.REPLY_ALREADY_DELETED);
		}
	}

	private boolean isDeleted() {
		return this.getDeletedAt() == null;
	}

	// public void reportReply(String reason) {
	// 	if (this.isReported) {
	// 		throw new CustomException(ErrorCode.REPLY_ALREADY_REPORTED);
	// 	}
	// 	this.isReported = true;
	// 	this.reportReason = reason;
	// }
	//
	// public void rollbackReportReply() {
	// 	if (!this.isReported) {
	// 		throw new CustomException(ErrorCode.REPLY_IS_NOT_REPORTED);
	// 	}
	// 	this.isReported = false;
	// 	this.reportReason = null;

	// }
}
