package com.workat.api.chat.dto.response;

import java.util.List;

import com.workat.api.chat.dto.ChatMessageDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessageResponse {

	private List<ChatMessageDto> messages;

	public static ChatMessageResponse of(List<ChatMessageDto> messages) {
		return new ChatMessageResponse(messages);
	}
}
