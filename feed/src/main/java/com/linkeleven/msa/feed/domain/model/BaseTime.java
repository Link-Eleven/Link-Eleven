package com.linkeleven.msa.feed.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Setter
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Setter
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private Long createdBy;

	@Setter
	@LastModifiedBy
	@Column(name = "updated_by")
	private Long updatedBy;

	@Setter
	@Column(name = "deleted_by")
	private Long deletedBy;
}
