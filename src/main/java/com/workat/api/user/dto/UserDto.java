package com.workat.api.user.dto;

import static lombok.AccessLevel.*;

import java.util.UUID;

import com.workat.domain.auth.OauthType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserDto {

	private UUID id;

	private String nickname;

	private String position;

	private int workingYear;

	private OauthType oauthType;

	private long oauthId;

	private float latitude;

	private float longitude;

	@Builder
	public UserDto(
		UUID id,
		String nickname,
		String position,
		int workingYear,
		OauthType oauthType,
		long oauthId,
		float latitude,
		float longitude
	) {
		this.id = id;
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
		this.oauthType = oauthType;
		this.oauthId = oauthId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
