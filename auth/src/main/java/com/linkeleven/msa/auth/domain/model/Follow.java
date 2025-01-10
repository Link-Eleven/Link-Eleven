package com.linkeleven.msa.auth.domain.model;

import java.time.LocalDateTime;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="p_follow")
public class Follow extends BaseTime{
	@Id
	@Tsid
	private Long followId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_user",nullable = false)
	private User following;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_user",nullable = false)
	private User follower;

	public static Follow createFollow(User follower, User following) {
		return Follow.builder()
			.follower(follower)
			.following(following)
			.build();
	}

	public void changeDelete(){
		this.setDeletedAt(null);
		this.setDeletedBy(null);
	}

	public void deleteUser(Long userId) {
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId);
	}
}
