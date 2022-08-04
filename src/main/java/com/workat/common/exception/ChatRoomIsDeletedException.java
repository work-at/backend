package com.workat.common.exception;

public class ChatRoomIsDeletedException extends NotFoundException {
	public ChatRoomIsDeletedException(long id) {
		super("chatRoom is deleted, id value = " + id);
	}

	public ChatRoomIsDeletedException(Throwable t) {
		super(t);
	}
}
