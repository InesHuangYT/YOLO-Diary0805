package com.example.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MySelfieNotFoundException extends RuntimeException  {
	  public MySelfieNotFoundException(String message) {
	        super(message);
	    }

	    public MySelfieNotFoundException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
