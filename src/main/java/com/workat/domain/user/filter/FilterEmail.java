package com.workat.domain.user.filter;

public enum FilterEmail {

	NAVER("@naver.com"),
	GMAIL("@gmail.com"),
	DAUM("@daum.net"),
	HANMAIL("@hanmail.net"),
	KAKAO("@kakao.com"),
	OUTLOOK("@outlook.com");

	private String email;

	FilterEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
