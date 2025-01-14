package com.linkeleven.msa.notification.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FcmToken {


	@Id
	private Long id;

	private String token;


}