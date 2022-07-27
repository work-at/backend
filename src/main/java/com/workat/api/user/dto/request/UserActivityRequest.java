package com.workat.api.user.dto.request;

import static lombok.AccessLevel.*;

import java.util.List;

import com.workat.common.annotation.IsValidListSize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserActivityRequest {

	@ApiModelProperty(name = "activities", notes = "유저 활동 타입")
	@IsValidListSize(min = 1, max = 3)
	private List<String> activities;

}
