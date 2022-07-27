package com.workat.common.annotation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
class ListSizeValidator implements ConstraintValidator<IsValidListSize, List<?>> {

	private int maxSize;
	private int minSize;

	@Override
	public boolean isValid(List<?> itemList, ConstraintValidatorContext cxt) {
		if (itemList.size() <= maxSize && itemList.size() >= minSize) {
			return true;
		} else {
			cxt.disableDefaultConstraintViolation();
			cxt.buildConstraintViolationWithTemplate(
				String.format("리스트 원소의 갯수는 최소 %s, 최대 %s 개 입니다.", minSize, maxSize))
				.addConstraintViolation();
			return false;
		}
	}

	@Override
	public void initialize(IsValidListSize constraintAnnotation) {
		this.maxSize = constraintAnnotation.max();
		this.minSize = constraintAnnotation.min();
	}
}
