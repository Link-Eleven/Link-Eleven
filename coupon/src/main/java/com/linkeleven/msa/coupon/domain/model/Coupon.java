package com.linkeleven.msa.coupon.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_coupon")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon extends BaseTime {
	@Id
	@Tsid
	private Long couponId;

	@Column(nullable = false)
	private Long feedId;

	@Column(updatable = false)
	private int quantity;

	@OneToMany(mappedBy = "coupon")
	private List<IssuedCoupon> issuedCoupons; // 발급된 쿠폰 리스트
	
	@Column(columnDefinition = "int default 0")
	private int issuedCount;

	@Column(nullable = false, updatable = false)
	private LocalDateTime validFrom;

	@Column(nullable = false, updatable = false)
	private LocalDateTime validTo;

	private int discountRate;
}
