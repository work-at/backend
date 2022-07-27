package com.workat.api.user.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmailLimitResponseDto {

	@ApiModelProperty(name = "remain", notes = "남은 이메일 인증 가능 횟수", example = "4")
	int remain;

	private EmailLimitResponseDto(int remain) {
		this.remain = remain;
	}

	public static EmailLimitResponseDto of(int remain) {
		return new EmailLimitResponseDto(remain);
	}
}
