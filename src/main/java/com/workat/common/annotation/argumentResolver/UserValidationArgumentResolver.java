package com.workat.common.annotation.argumentResolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.workat.api.jwt.service.JwtService;
import com.workat.api.user.service.UserService;
import com.workat.common.annotation.UserValidation;
import com.workat.common.exception.UnAuthorizedException;
import com.workat.domain.user.entity.Users;
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
		return methodParameter.hasParameterAnnotation(UserValidation.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter methodParameter,
		ModelAndViewContainer modelAndViewContainer,
		NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {

		final String jwt = parseTokenFromHeader();

		if (jwtService.isTokenExpired(jwt)) {
			throw new UnAuthorizedException("Access token is expired");
		}

		final long id = extractIdFromJWT(jwt);

		final Users user = userService.findUserWithId(id)
			.orElseThrow(() -> {
				throw new UnAuthorizedException("Please login again");
			});

		return user;
	}

	private long extractIdFromJWT(String jwt) {
		return jwtService.getValueFromJWT(jwt, "id", Long.class);
	}

	private String parseTokenFromHeader() {
		final String requestTokenHeader = httpServletRequest.getHeader("Authorization");

		if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
			throw new UnAuthorizedException("No Bearer header found");
		}

		return requestTokenHeader.split("Bearer ")[1];
	}

}
