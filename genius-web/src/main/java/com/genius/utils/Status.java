package com.genius.utils;

public class Status {

	private String message;
	private String type;// success or error or warn

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String WARN = "warn";

	public Status() {
		super();
	}

	public Status(String type) {
		super();
		this.type = type;
	}

	public Status(String type, String message) {
		super();
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
