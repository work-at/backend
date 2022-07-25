package com.workat.api.chat.dto;

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

	public static ChatRoomDto of(Long id, Long user1Id, Long user2Id) {
		return new ChatRoomDto(id, List.of(user1Id, user2Id));
	}
}
