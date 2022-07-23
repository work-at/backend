package com.workat.api.user.dto.request;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Email;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EmailCertifyRequest {

	@ApiModelProperty(name = "email", notes = "회사 메일", example = "\"workat.test.mail@gmail.com\"")
	@Email
	private String email;

}
