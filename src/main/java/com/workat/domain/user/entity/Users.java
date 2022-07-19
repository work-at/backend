package com.workat.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.workat.domain.BaseEntity;
import com.workat.domain.auth.OauthType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = @Index(columnList = "oauthId", unique = true))
public class Users extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column
	private OauthType oauthType;

	@Column(unique = true)
	private Long oauthId;

	@OneToOne
	@JoinColumn(name = "profile_id")
	private UserProfile userProfile;

	private Users(OauthType oauthType, Long oauthId) {
		this.oauthType = oauthType;
		this.oauthId = oauthId;
	}

	public static Users of(OauthType oauthType, Long oauthId) {
		return new Users(oauthType, oauthId);
	}

	public void setUserProfile(UserProfile userProfile){
		this.userProfile = userProfile;
	}
}
