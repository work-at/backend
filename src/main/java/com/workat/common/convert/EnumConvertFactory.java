package com.workat.common.convert;

import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class EnumConvertFactory implements ConverterFactory<String, Enum<? extends ConvertContent>> {

	private static final RuntimeException ENUM_CONVERTER_NAME_NOT_MATCHING = new RuntimeException("enum converter name not matching");

	@Override
	public <T extends Enum<? extends ConvertContent>> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToEnumsConverter<>(targetType);
	}

	private static final class StringToEnumsConverter<T extends Enum<? extends ConvertContent>> implements Converter<String, T> {

		private final Class<T> enumType;
		private final boolean isConvertContent;

		public StringToEnumsConverter(Class<T> enumType) {
			this.enumType = enumType;
			this.isConvertContent = Arrays.stream(enumType.getInterfaces()).anyMatch(i -> i == ConvertContent.class);
		}

		@Override
		public T convert(String source) {
			if (isConvertContent) {
				return Arrays.stream(enumType.getEnumConstants())
					.filter(contant -> contant.name().equalsIgnoreCase(source))
					.findFirst()
					.orElseThrow(() -> ENUM_CONVERTER_NAME_NOT_MATCHING);

			} else {
				throw new RuntimeException("enum converter type not matching");
			}
		}
	}
}
