package com.linkeleven.msa.auth.domain.model;

import com.linkeleven.msa.auth.domain.common.UserRole;

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
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name="p_user")
public class User extends BaseTime {
	@Id
	@Tsid
	private Long userId;

	@Column(name="username",nullable = false)
	private String username;

	@Column(name="password",nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name="role",nullable = false)
	private UserRole role;

	@Column(name="is_anonymous",nullable = false)
	private Boolean isAnonymous;

	@Column(name="is_coupon_issued")
	private Boolean isCouponIssued;
	public static User createUser(String username, String password, UserRole role, Boolean isAnonymous) {
		return User.builder()
			.username(username)
			.password(password)
			.role(role)
			.isAnonymous(isAnonymous)
			.isCouponIssued(true)
			.build();
	}

	public void updateAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public void updateCouponIssued(Boolean isCouponIssued) {
		this.isCouponIssued = isCouponIssued;
	}
	public void updateUsername(String username) {
		this.username = username;
	}
}
