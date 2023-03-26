package gg.bayes.challenge.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DotaException.class)
	public ResponseEntity<?> handleDotaxception(DotaException exception, WebRequest request){
		
		ErrorDetails error= new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false),exception.getStatus());
		
		return new ResponseEntity(error,error.getStatus());
		
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request){
		
		ErrorDetails error= new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		
		return new ResponseEntity(error,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

}
