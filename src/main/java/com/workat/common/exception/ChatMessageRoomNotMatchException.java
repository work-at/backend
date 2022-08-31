package com.workat.common.exception;

public class ChatMessageRoomNotMatchException extends ConflictException {
	public ChatMessageRoomNotMatchException(long messageRoomId, long inputRoom) {
		super("chatMessage not matching room, chatMessageRoomId value = " + messageRoomId + ", inputRoom value = " + inputRoom);
	}

	public ChatMessageRoomNotMatchException(Throwable t) {
		super(t);
	}
}
