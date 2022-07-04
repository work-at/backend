package com.workat.common.annotation.argumentResolver;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.workat.api.auth.dto.KakaoOauthTokenInfoResponse;
import com.workat.api.jwt.service.JwtService;
import com.workat.api.user.service.UserService;
import com.workat.common.exception.NotFoundException;
import com.workat.common.exception.UnAuthorizedException;
import com.workat.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserValidationArgumentResolver implements HandlerMethodArgumentResolver {

	private final HttpServletRequest httpServletRequest;

	private final JwtService jwtService;

	private final UserService userService;

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterType().equals(KakaoOauthTokenInfoResponse.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter methodParameter,
		ModelAndViewContainer modelAndViewContainer,
		NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {

		final String jwt = parseTokenFromHeader();

		final UUID id = extractIdFromJWT(jwt);

		final User user = userService
			.validateUserExistWithId(id)
			.orElseThrow(() -> {
				throw new NotFoundException("User does not exist");
			});

		return user;
	}

	private UUID extractIdFromJWT(String jwt) {
		final String strId = jwtService.getValueFromJWT(jwt, "id");

		return UUID.fromString(strId);
	}

	private String parseTokenFromHeader() {
		final String requestTokenHeader = httpServletRequest.getHeader("Authorization")
															.substring(7);

		if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
			throw new UnAuthorizedException("No Bearer header found");
		}

		return requestTokenHeader.split("Bearer ")[0];
	}

}
