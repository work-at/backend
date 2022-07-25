package com.workat.api.chat.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessageDto {

	private Long id;

	private Long writerId;

	private String message;

	public static ChatMessageDto of(Long id, Long writerId, String message) {
		return new ChatMessageDto(id, writerId, message);
	}
}
