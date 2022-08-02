package com.workat.common.exception;

public class ChatMessageNotFoundException extends NotFoundException {
	public ChatMessageNotFoundException(long id) {
		super("chatMessage not found, id value = " + id);
	}

	public ChatMessageNotFoundException(Throwable t) {
		super(t);
	}
}
