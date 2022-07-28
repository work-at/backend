package com.workat.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ListSizeValidator.class)
public @interface IsValidListSize {
	String message() default "";

	int max() default 10;

	int min() default 0;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
