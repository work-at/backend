package com.workat.api.chat.dto.response;

import org.springframework.data.domain.Page;

import com.workat.api.chat.dto.ChatMessageDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessageResponse {

	private Page<ChatMessageDto> messages;

	public static ChatMessageResponse of(Page<ChatMessageDto> messages) {
		return new ChatMessageResponse(messages);
	}
}
