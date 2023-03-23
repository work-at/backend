package com.workat.common.config;

import com.workat.common.convert.EnumConvertFactory;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.workat.api.chat.service.converter.ChatMessageSortTypeConverter;
import com.workat.common.annotation.argumentResolver.UserValidationArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${resources.upload-folder:/home/work_at_tour/images}")
	private String resourcesLocation;
	@Value("${resources.upload-uri:/uploaded}")
	private String uploadUri;

	@Autowired
	private UserValidationArgumentResolver validationArgumentResolver;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		WebMvcConfigurer.super.addFormatters(registry);
		//Converter
		registry.addConverter(new ChatMessageSortTypeConverter());
		//ConvertFactory
		registry.addConverterFactory(new EnumConvertFactory());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(validationArgumentResolver);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(3600L);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler(uploadUri + "/**").addResourceLocations("file:///" + resourcesLocation + "/");
	}
}
