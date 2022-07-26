package com.workat.common.exception;

public class ChatRoomNotFoundException extends NotFoundException {
	public ChatRoomNotFoundException(long id) {
		super("chatRoom not found, id value = " + id);
	}

	public ChatRoomNotFoundException(Throwable t) {
		super(t);
	}
}
