package com.workat.common.util;

import javax.servlet.http.HttpServletRequest;

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
}
