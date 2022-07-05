package com.workat.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

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
@Table(indexes = @Index(columnList = "oauthId", unique = true))
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String nickname;

	@Column
	private OauthType oauthType;

	@Column(unique = true)
	private Long oauthId;

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

	// TODO: image 와 자기소개는 마이페이지에서 수정한다고 생각하고 일단 진행함

	@Column
	private String imageUrl;

	@Column
	private String story;

	@Builder
	public Users(String nickname, OauthType oauthType, Long oauthId, DepartmentType position, DurationType workingYear,
		double latitude, double longitude, String imageUrl, String story) {
		this.nickname = nickname;
		this.oauthType = oauthType;
		this.oauthId = oauthId;
		this.position = position;
		this.workingYear = workingYear;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
		this.story = story;
	}
}