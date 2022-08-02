package com.workat.api.chat.service.converter;

import org.springframework.core.convert.converter.Converter;

import com.workat.domain.chat.entity.ChatMessageSortType;

public class ChatMessageSortTypeConverter implements Converter<String, ChatMessageSortType> {

	@Override
	public ChatMessageSortType convert(String source) {
		return ChatMessageSortType.of(source);
	}
}
