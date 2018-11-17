package com.example.payload;

import com.example.entity.Selfie;

public class FindFriendResponse {
	
	private String friendname;
	private String selfieURI;
	
	
	

	public FindFriendResponse(String friendname, String selfieURI) {
		this.friendname = friendname;
		this.selfieURI = selfieURI;
	}
	
	
	
	
	public String getFriendname() {
		return friendname;
	}
	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}
	public String getSelfieURI() {
		return selfieURI;
	}
	public void setSelfieURI(String selfieURI) {
		this.selfieURI = selfieURI;
	}
	
}
