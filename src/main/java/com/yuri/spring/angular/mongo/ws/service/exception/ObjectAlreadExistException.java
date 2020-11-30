package com.yuri.spring.angular.mongo.ws.service.exception;

import java.io.Serializable;

public class ObjectAlreadExistException extends RuntimeException implements Serializable{

	private static final long serialVersionUID = 1L;

	public ObjectAlreadExistException(String message) {
		super(message);
	}
}
