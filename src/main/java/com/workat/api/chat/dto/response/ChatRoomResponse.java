package com.workat.api.chat.dto.response;

import java.util.List;

import com.workat.api.chat.dto.ChatRoomDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomResponse {

	private List<ChatRoomDto> rooms;

	public static ChatRoomResponse of(List<ChatRoomDto> rooms) {
		return new ChatRoomResponse(rooms);
	}
}
