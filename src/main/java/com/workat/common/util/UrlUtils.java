package com.workat.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlUtils {

	public static String getBaseUrl(HttpServletRequest request) {
		String baseUrl = ServletUriComponentsBuilder
			.fromRequestUri(request)
			.replacePath(null)
			.build()
			.toUriString();

		return baseUrl;
	}

	public static String getBaseUrl() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return ServletUriComponentsBuilder
			.fromRequestUri(req)
			.replacePath(null)
			.build()
			.toUriString();
	}
}
