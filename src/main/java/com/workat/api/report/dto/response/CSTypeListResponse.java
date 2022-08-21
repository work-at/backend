package com.workat.api.report.dto.response;

import java.util.List;

import com.workat.domain.report.CSType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CSTypeListResponse {

	@ApiModelProperty(name = "response", notes = "cs 타입")
	private List<CSType> CSTypes;

	private CSTypeListResponse(List<CSType> CSTypes) {
		this.CSTypes = CSTypes;
	}

	public static CSTypeListResponse of(List<CSType> CSTypes) {
		return new CSTypeListResponse(CSTypes);
	}
}
