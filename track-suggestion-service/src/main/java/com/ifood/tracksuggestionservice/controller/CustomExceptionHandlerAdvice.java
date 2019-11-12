package com.ifood.tracksuggestionservice.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.validation.ConstraintViolationException;
import java.util.Locale;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandlerAdvice {
	
	private static final String CITY_NOT_FOUND = "city.not-found";
	private static final String CLIENT_INTERNAL_SERVER_ERROR = "client.internal-server-error";
	
	private final MessageSource messageSource;
	
	@Autowired
	public CustomExceptionHandlerAdvice(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ExceptionHandler(CityNotFoundException.class)
	public ResponseEntity<CustomExceptionResponse> handleCityNotFoundException(CityNotFoundException exception) {
		logMessageStackTrace(exception);
		
		final CustomExceptionResponse response = getCustomExceptionResponse(CITY_NOT_FOUND);
		return ResponseEntity.status(NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(ClientException.class)
	public ResponseEntity<CustomExceptionResponse> handleClientException(ClientException exception) {
		logMessageStackTrace(exception);
		
		final CustomExceptionResponse response = getCustomExceptionResponse(CLIENT_INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
	}
	
	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
		logMessageStackTrace(exception);
		
		final CustomExceptionResponse response = CustomExceptionResponse.builder().message(exception.getMessage()).build();
		return ResponseEntity.badRequest().body(response);
	}
	
	private void logMessageStackTrace(Exception exception) {
		log.debug("Handling {}: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
	}
	
	private CustomExceptionResponse getCustomExceptionResponse(String code) {
		final String message = messageSource.getMessage(code, null, Locale.getDefault());
		return CustomExceptionResponse.builder().message(message).build();
	}
	
}
