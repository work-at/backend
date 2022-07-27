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

import net.bytebuddy.utility.RandomString;

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

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column
	private OauthType oauthType;

	@Column(unique = true)
	private Long oauthId;

	@Column
	private String verificationCode;

	@Column
	private int emailRequestRemain = 5;

	private Users(OauthType oauthType, Long oauthId) {
		this.oauthType = oauthType;
		this.oauthId = oauthId;
	}

	public static Users of(OauthType oauthType, Long oauthId) {
		return new Users(oauthType, oauthId);
	}

	public void setVerificationCode() {
		this.verificationCode = RandomString.make(64);
	}

	public void clearVerificationCode() {
		this.verificationCode = null;
	}

	public void decreaseEmailRequestRemain() {
		emailRequestRemain--;
	}
}
