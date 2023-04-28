package com.learncamel.domain;

public class Response {

	int errorCode;
	String errorMessage;
	String message;
	
	protected Response(int errorCode, String errorMessage, String message) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.message = message;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
