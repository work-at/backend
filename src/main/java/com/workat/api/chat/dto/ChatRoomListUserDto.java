package com.workat.api.chat.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomListUserDto {

	private boolean isOwner;

	private Long userId;

	private String userNickname;

	private String userProfileUrl;

	private String position;

	private String workingYear;

}
