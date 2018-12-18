package com.example.payload;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendTagEmailRequest {
	
	private String[] name;

	public SendTagEmailRequest(@JsonProperty("theArray") String[] name) {
		super();
		this.name = name;
	}
	

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}
	
	
	

}
