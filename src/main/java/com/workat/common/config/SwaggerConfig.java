package com.workat.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.google.common.collect.Sets;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final String API_NAME = "Workat API";

	private static final String API_VERSION = "1.0";


	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(getApiInfo())
			.produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
			.consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.workat"))
			.paths(PathSelectors.any())
			.build();
	}

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
			.title(API_NAME)
			.version(API_VERSION)
			.build();
	}

}
