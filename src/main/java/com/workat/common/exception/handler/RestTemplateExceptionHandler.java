package com.workat.common.exception.handler;

import static org.springframework.http.HttpStatus.Series.*;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workat.common.exception.base.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RestTemplateExceptionHandler implements ResponseErrorHandler {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return (
			httpResponse.getStatusCode().series() == CLIENT_ERROR
				|| httpResponse.getStatusCode().series() == SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		throw new BusinessException(httpResponse.getStatusCode(), objectMapper.readValue(httpResponse.getBody(), Object.class).toString());
	}
}
