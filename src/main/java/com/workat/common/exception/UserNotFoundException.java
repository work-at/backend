package com.workat.common.exception;

public class UserNotFoundException extends NotFoundException {

	public UserNotFoundException(long id) {
		super("user not found, id value = " + id);
	}

	public UserNotFoundException(Throwable t) {
		super(t);
	}
}
