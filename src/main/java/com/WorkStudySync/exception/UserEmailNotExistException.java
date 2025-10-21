package com.WorkStudySync.exception;

public class UserEmailNotExistException extends RuntimeException {
	public UserEmailNotExistException(String message) {
		super(message);
	}
	
	private static final long serialVersionUID = 1L;

}
