package com.workat.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.workat.domain.auth.OauthType;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String nickname;

	@Column
	private OauthType oauthType;

	@Column(unique = true)
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
	public Users(String nickname, OauthType oauthType, long oauthId, DepartmentType position, DurationType workingYear,
		double latitude, double longitude, String imageUrl) {
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
