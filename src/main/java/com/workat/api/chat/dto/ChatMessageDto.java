package com.workat.api.chat.dto;

import java.time.LocalDateTime;

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

	private LocalDateTime createdDate;

	public static ChatMessageDto of(Long id, Long writerId, String message, LocalDateTime createdDate) {
		return new ChatMessageDto(id, writerId, message, createdDate);
	}
}
