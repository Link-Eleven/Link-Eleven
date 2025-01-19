package com.linkeleven.msa.coupon.domain.model;

import java.time.LocalDateTime;

import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_issued_coupon")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssuedCoupon extends BaseTime {
	@Id
	@Tsid
	private Long issuedCouponId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long couponId;

	@Column(nullable = false)
	private int discountRate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private IssuedCouponStatus status;

	public static IssuedCoupon of(Long userId, Long couponId, int discountRate) {
		return IssuedCoupon.builder()
			.userId(userId)
			.couponId(couponId)
			.discountRate(discountRate)
			.status(IssuedCouponStatus.ISSUED)
			.build();
	}

	public void markAsUsed() {
		validateCanBeUsed();
		this.status = IssuedCouponStatus.USED;
	}

	private void validateCanBeUsed() {
		if (status != IssuedCouponStatus.ISSUED) {
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}
	}

	public void softDelete(Long userId) {
		this.setDeletedBy(userId);
		this.setDeletedAt(LocalDateTime.now());
		this.status = IssuedCouponStatus.DELETED;
	}
}
