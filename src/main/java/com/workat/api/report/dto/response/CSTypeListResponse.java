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

	@ApiModelProperty(name = "response", notes = "cs 타입", example = "{\"response\":[\"INQUERY\",\"SERVICE_OFFER\",\"ERROR_REPORT\",\"OTHERS\"]}")
	private List<CSType> response;

	private CSTypeListResponse(List<CSType> response) {
		this.response = response;
	}

	public static CSTypeListResponse of(List<CSType> response) {
		return new CSTypeListResponse(response);
	}
}
