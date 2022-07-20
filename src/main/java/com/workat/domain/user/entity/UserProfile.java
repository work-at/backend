package com.workat.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.workat.domain.BaseEntity;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserProfile extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "user_id")
	private Users user;

	@NotNull
	@Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|]{2,8}$")
	@Column(unique = true)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column
	private DepartmentType position;

	@Enumerated(EnumType.STRING)
	@Column
	private DurationType workingYear;

	@Column
	private String imageUrl;

	@Column
	private String story;

	@Column
	private boolean certified;

	@Builder
	public UserProfile(Users user, String nickname, DepartmentType position, DurationType workingYear, String imageUrl, String story, boolean certified) {
		this.id = user.getId();
		this.user = user;
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
		this.imageUrl = imageUrl;
		this.story = story;
		this.certified = certified;
	}

	public void updateProfile(String nickname, DepartmentType position, DurationType workingYear, String story) {
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
		this.story = story;
	}
}
