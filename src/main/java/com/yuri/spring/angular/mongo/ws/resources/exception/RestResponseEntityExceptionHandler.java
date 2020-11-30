package com.yuri.spring.angular.mongo.ws.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.yuri.spring.angular.mongo.ws.service.exception.ObjectAlreadExistException;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotEnableException;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandartError> objectNotFound(ObjectNotFoundException e, HttpServletRequest http){
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandartError standart = new StandartError(System.currentTimeMillis(),status.value(),"Não encontrado",e.getMessage(),http.getRequestURI());
		return ResponseEntity.status(status).body(standart);
	}
	
	@ExceptionHandler(ObjectAlreadExistException.class)
	public ResponseEntity<Object> handlerObjectAlreadyExist(RuntimeException re, HttpServletRequest request){
		HttpStatus status = HttpStatus.CONFLICT;
		StandartError error = new StandartError(System.currentTimeMillis(),status.value(),"UserAlreadyExist",re.getMessage(),request.getRequestURI().toString());
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(ObjectNotEnableException.class)
	public ResponseEntity<Object> handleObjectNotEnabled(RuntimeException re,HttpServletRequest request){
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandartError error = new StandartError(System.currentTimeMillis(),status.value(),"UserNotEnabled",re.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(error);
	}
	
}
