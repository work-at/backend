package com.workat.api.chat.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomDto {

	private Long id;

	private ChatRoomListUserDto otherUser;

	private Long lastMessageId;

	private String lastMessage;

	private boolean isStart;

	private boolean isAllRead;

	private boolean isBlockedByOtherUser;

	private LocalDateTime createdDate;
}
