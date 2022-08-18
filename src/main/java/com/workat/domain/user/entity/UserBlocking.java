package com.workat.domain.user.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.workat.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserBlocking extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "reporting_user_id")
	@ManyToOne
	private Users reportingUser;

	@JoinColumn(name = "blocked_user_id")
	@ManyToOne
	private Users blockedUser;

	public static UserBlocking of() {
		return new UserBlocking();
	}

	public void assignUsers(Users reportingUser, Users blockedUser) {
		this.reportingUser = reportingUser;
		this.blockedUser = blockedUser;
	}
}
