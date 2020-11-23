package com.yuri.spring.angular.mongo.ws.repository.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandartError> objectNotFound(ObjectNotFoundException e, HttpServletRequest http){
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandartError standart = new StandartError(System.currentTimeMillis(),status.value(),"Não encontrado",e.getMessage(),http.getRequestURI());
		return ResponseEntity.status(status).body(standart);
	}
	
}
