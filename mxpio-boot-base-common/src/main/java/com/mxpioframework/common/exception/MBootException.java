package com.mxpioframework.common.exception;

public class MBootException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MBootException(String message) {
		super(message);
	}

	public MBootException(String message, Throwable cause) {
		super(message, cause);
	}

	public MBootException(Throwable cause) {
		super(cause);
	}
}
