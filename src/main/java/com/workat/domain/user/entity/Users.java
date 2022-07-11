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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.workat.domain.auth.OauthType;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
	@ApiModelProperty(hidden = true)
	private Long id;

	@NotNull
	@Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|]{2,8}$")
	@Column(unique = true)
	@ApiModelProperty(hidden = true)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column
	@ApiModelProperty(hidden = true)
	private OauthType oauthType;

	@Column(unique = true)
	@ApiModelProperty(hidden = true)
	private Long oauthId;

	@Enumerated(EnumType.STRING)
	@Column
	@ApiModelProperty(hidden = true)
	private DepartmentType position;

	@Enumerated(EnumType.STRING)
	@Column
	@ApiModelProperty(hidden = true)
	private DurationType workingYear;

	// TODO: image 와 자기소개는 마이페이지에서 수정한다고 생각하고 일단 진행함

	@Column
	@ApiModelProperty(hidden = true)
	private String imageUrl;

	@Column
	@ApiModelProperty(hidden = true)
	private String story;

	@Builder
	public Users(String nickname, OauthType oauthType, Long oauthId, DepartmentType position, DurationType workingYear,
		String imageUrl, String story) {
		this.nickname = nickname;
		this.oauthType = oauthType;
		this.oauthId = oauthId;
		this.position = position;
		this.workingYear = workingYear;
		this.imageUrl = imageUrl;
		this.story = story;
	}
}
