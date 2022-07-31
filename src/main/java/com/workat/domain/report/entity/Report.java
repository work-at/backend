package com.workat.domain.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.workat.domain.report.CSType;
import com.workat.domain.user.entity.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Report {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;

	@Column
	private String email;

	@Enumerated(EnumType.STRING)
	private CSType csType;

	@Column
	private String content;

	private Report(Users user, String email, CSType csType, String content) {
		this.user = user;
		this.email = email;
		this.csType = csType;
		this.content = content;
	}

	public static Report of(Users user, String email, CSType csType, String content) {
		return new Report(user, email, csType, content);
	}
}
