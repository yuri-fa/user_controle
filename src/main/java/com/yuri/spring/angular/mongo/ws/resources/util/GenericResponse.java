package com.yuri.spring.angular.mongo.ws.resources.util;

import java.io.Serializable;

public class GenericResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String message;
	private String error;
	
	public GenericResponse(String message, String error) {
		super();
		this.message = message;
		this.error = error;
	}
	
	
	public GenericResponse(String message) {
		super();
		this.message = message;
	}


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
