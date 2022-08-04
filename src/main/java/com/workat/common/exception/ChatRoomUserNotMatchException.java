package com.workat.common.exception;

public class ChatRoomUserNotMatchException extends ConflictException {
	public ChatRoomUserNotMatchException(long roomId, long userId) {
		super("chatRoom not matching user, roomId value = " + roomId + ", userId value = " + userId);
	}

	public ChatRoomUserNotMatchException(Throwable t) {
		super(t);
	}
}
