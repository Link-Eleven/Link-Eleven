package com.linkeleven.msa.coupon.domain.model;

import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;

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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private IssuedCouponStatus status = IssuedCouponStatus.ISSUED;

	public static IssuedCoupon of(Long userId, Long couponId, IssuedCouponStatus status) {
		return IssuedCoupon.builder()
			.userId(userId)
			.couponId(couponId)
			.status(status)
			.build();
	}
}
