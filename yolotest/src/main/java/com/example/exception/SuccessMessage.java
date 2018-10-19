package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class SuccessMessage extends RuntimeException {
	
	private String user;
	private String friend;
	
	public SuccessMessage(String user, String friend) {
		super(String.format("%s add %s to friend list", user, friend));
		this.user = user;
		this.friend = friend;
	}

	public String getUser() {
		return user;
	}


	public String getFriend() {
		return friend;
	}

	
	
	

}
