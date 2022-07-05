package com.workat.api.user.dto.response;

import java.util.List;

import com.workat.api.user.dto.JobTypeDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JobTypeListResponse {

	@ApiModelProperty(name = "response", notes = "직무 타입", example = "{\"response\":[{\"name\":\"IT_ENGINEER\",\"content\":\"IT 엔지니어 및 보안\"},{\"name\":\"DESIGNER\",\"content\":\"디자이너\"},{\"name\":\"IT_PRODUCT_MANAGER\",\"content\":\"IT 기획 및 매니지먼트\"},{\"name\":\"MARKETING\",\"content\":\"마케팅, 홍보, 광고\"},{\"name\":\"MERCHANDISER\",\"content\":\"상품 기획, MD\"},{\"name\":\"BUSINESS_MANAGER\",\"content\":\"경영전략, 관리\"},{\"name\":\"HUMAN_RESOURCE\",\"content\":\"인사, 노무, 법무\"},{\"name\":\"SUPPLY_MANAGER\",\"content\":\"구매, 물류, SCM\"},{\"name\":\"SALES\",\"content\":\"영업, 해외영업\"},{\"name\":\"ACCOUNTANT\",\"content\":\"회계, 재무, 세무\"},{\"name\":\"TRADE\",\"content\":\"유통, 무역\"},{\"name\":\"FINANCE\",\"content\":\"금융\"},{\"name\":\"OTHERS\",\"content\":\"기타\"}]}")
	List<JobTypeDto> response;

	private JobTypeListResponse(List<JobTypeDto> response) {
		this.response = response;
	}

	public static JobTypeListResponse of(List<JobTypeDto> response) {
		return new JobTypeListResponse(response);
	}
}
