package com.workat.api.user.dto.response;

import java.util.List;

import com.workat.api.user.dto.UserTypeDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserTypeListResponse {

	@ApiModelProperty(name = "response", notes = "선택 타입 리스트")
	List<UserTypeDto> response;

	private UserTypeListResponse(List<UserTypeDto> response) {
		this.response = response;
	}

	public static UserTypeListResponse of(List<UserTypeDto> response) {
		return new UserTypeListResponse(response);
	}
}
