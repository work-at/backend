package com.workat.domain.user;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.workat.domain.auth.OauthType;

import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.job.DepartmentType;
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
	private String id;

	@Column
	private String nickname;

	@Column
	private OauthType oauthType;

	@Column
	private long oauthId;

	@Enumerated(EnumType.STRING)
	@Column
	private DepartmentType position;

	@Enumerated(EnumType.STRING)
	@Column
	private DurationType workingYear;

	@Column
	private double latitude;

	@Column
	private double longitude;

	@Column
	private String imageUrl;

	@Builder
	public User(UUID id, String nickname, OauthType oauthType, long oauthId, DepartmentType position, DurationType workingYear,
		double latitude, double longitude, String imageUrl) {
		this.id = id.toString();
		this.nickname = nickname;
		this.oauthType = oauthType;
		this.oauthId = oauthId;
		this.position = position;
		this.workingYear = workingYear;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
	}
}
