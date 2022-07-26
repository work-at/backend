package com.workat.api.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomDto {

	private Long id;

	private List<Long> ownerUserIds;

	private LocalDateTime createdDate;

	public static ChatRoomDto of(Long id, Long ownerUserId, Long otherUserId, LocalDateTime createdDate) {
		return new ChatRoomDto(id, List.of(ownerUserId, otherUserId), createdDate);
	}
}
