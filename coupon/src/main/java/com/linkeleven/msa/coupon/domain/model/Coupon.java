package com.linkeleven.msa.coupon.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_coupon")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Coupon extends BaseTime {
	@Id
	@Tsid
	private Long couponId;

	@Column(nullable = false)
	private Long feedId;

	@Column(nullable = false, updatable = false)
	private LocalDateTime validFrom;

	@Column(nullable = false, updatable = false)
	private LocalDateTime validTo;

	@OneToMany(mappedBy = "couponId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CouponPolicy> policies;  // 쿠폰 정책 목록

	public static Coupon of(Long feedId,
		LocalDateTime validFrom, LocalDateTime validTo) {
		return Coupon.builder()
			.feedId(feedId)
			.validFrom(validFrom)
			.validTo(validTo)
			.build();
	}
}
