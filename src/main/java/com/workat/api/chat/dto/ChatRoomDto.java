package com.workat.api.chat.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomDto {

	private Long id;

	private ChatRoomListUserDto otherUser;

	private boolean isAllRead;

	private LocalDateTime createdDate;

	public static ChatRoomDto of(Long id, ChatRoomListUserDto otherUser, boolean isAllRead, LocalDateTime createdDate) {
		return new ChatRoomDto(id, otherUser, isAllRead, createdDate);
	}
}
