package com.workat.domain.user.filter;

import java.util.EnumSet;
import java.util.Set;

public enum FilterEmail {

	NAVER("@naver.com"),
	GMAIL("@gmail.com"),
	DAUM("@daum.net"),
	HANMAIL("@hanmail.net"),
	KAKAO("@kakao.com"),
	OUTLOOK("@outlook.com");

	private String email;

	private static final Set<FilterEmail> ALL = EnumSet.allOf(FilterEmail.class);

	FilterEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public static boolean anyMatch(String email) {
		return ALL
			.stream()
			.anyMatch(filterEmail -> email.endsWith(filterEmail.getEmail()));
	}
}
