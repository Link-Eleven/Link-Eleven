package com.linkeleven.msa.coupon.domain.model;

import java.time.LocalDateTime;

import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
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
@Table(name = "p_coupon_policy")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponPolicy extends BaseTime {
	@Id
	@Tsid
	private Long policyId;

	@Column(nullable = false)
	private Long couponId;

	@Column(nullable = false)
	private int discountRate;

	@Column(nullable = false)
	private int quantity;

	private int issuedCount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponPolicyStatus status;

	public static CouponPolicy of(Long couponId, int discountRate, int quantity) {
		return CouponPolicy.builder()
			.couponId(couponId)
			.discountRate(discountRate)
			.quantity(quantity)
			.issuedCount(0)  // 초기 발급 수는 0으로 설정
			.status(CouponPolicyStatus.ACTIVE)
			.build();
	}

	// issuedCount 증가 및 검증 메서드
	public void issueCoupon() {
		if (this.issuedCount < this.quantity) {
			this.issuedCount++;
		} else {
			throw new CustomException(ErrorCode.NO_AVAILABLE_COUPON);
		}
	}

	public void softDelete(Long userId) {
		this.status = CouponPolicyStatus.DELETED;
		this.setDeletedBy(userId);
		this.setDeletedAt(LocalDateTime.now());
	}
}