package com.workat.api.report.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.workat.domain.report.CSType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReportRequest {

	@Email
	@ApiModelProperty(name = "email", notes = "답변받을 메일", example = "workat@gmail.com")
	private String email;

	@NotNull
	@ApiModelProperty(name = "type", notes = "문의 타입(INQUERY, SERVICE_OFFER, ERROR_REPORT, OTHERS)", example = "workat@gmail.com")
	private CSType type;

	@NotBlank
	@ApiModelProperty(name = "content", notes = "문의 내용")
	private String content;

	private ReportRequest(String email, CSType type, String content) {
		this.email = email;
		this.type = type;
		this.content = content;
	}
}
