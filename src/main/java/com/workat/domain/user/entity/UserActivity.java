package com.workat.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.workat.domain.user.activity.ActivityType;

public class UserActivity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;

	@Enumerated(EnumType.STRING)
	@Column
	private ActivityType activity;

	private UserActivity(Users user, ActivityType activity) {
		this.user = user;
		this.activity = activity;
	}

	public static UserActivity of(Users user, ActivityType activity) {
		return new UserActivity(user, activity);
	}
}
