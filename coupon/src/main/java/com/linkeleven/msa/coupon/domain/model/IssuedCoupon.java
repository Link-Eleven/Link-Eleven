package com.linkeleven.msa.coupon.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "p_issuedcoupon")
public class IssuedCoupon extends BaseTime {
	@Id
	@Tsid
	private Long issuedCouponId;
	
	@Column(nullable = false)
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "coupon_id")
	private Coupon coupon; // 발급된 쿠폰

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.ISSUED;

	public enum Status {
		ISSUED, USED, EXPIRED
	}
}
