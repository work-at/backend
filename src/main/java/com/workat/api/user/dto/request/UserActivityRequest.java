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
	@IsValidListSize(min = 1, max = 3, message = "1개 이상 3개 이하의 리스트가 인풋으로 들어와야합니다")
	private List<String> activities;

}
