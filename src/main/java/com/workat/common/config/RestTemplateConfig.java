package com.workat.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.workat.common.exception.handler.RestTemplateExceptionHandler;

@Configuration
public class RestTemplateConfig {

	@Value("${restTemplate.factory.readTimeout}")
	private int READ_TIMEOUT;

	@Value("${restTemplate.factory.connectTimeout}")
	private int CONNECT_TIMEOUT;
	@Value("${restTemplate.httpClient.maxConnTotal}")
	private int MAX_CONN_TOTAL;
	@Value("${restTemplate.httpClient.maxConnPerRoute}")
	private int MAX_CONN_PER_ROUTE;

	@Bean
	HttpClient httpClient() {
		return HttpClientBuilder.create()
			.setMaxConnTotal(MAX_CONN_TOTAL)
			.setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
			.build();
	}

	@Bean
	HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(READ_TIMEOUT);
		factory.setConnectTimeout(CONNECT_TIMEOUT);
		factory.setHttpClient(httpClient);

		return factory;
	}

	@Bean
	RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setErrorHandler(new RestTemplateExceptionHandler());
		return restTemplate;
	}
}
