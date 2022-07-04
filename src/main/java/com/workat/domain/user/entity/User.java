package com.workat.domain.user.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.workat.domain.auth.OauthType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = @Index(columnList = "oauthId", unique = true))
public class User {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column
	private String nickname;

	@Column
	private OauthType oauthType;

	@Column
	private long oauthId;

	@Column
	private String position;

	@Column
	private int workingYear;

	@Column
	private float latitude;

	@Column
	private float longitude;

	@Builder
	public User(UUID id, String nickname, OauthType oauthType, long oauthId, String position, int workingYear,
		float latitude, float longitude) {
		this.id = id;
		this.nickname = nickname;
		this.oauthType = oauthType;
		this.oauthId = oauthId;
		this.position = position;
		this.workingYear = workingYear;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
