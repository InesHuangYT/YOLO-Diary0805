package com.example.payload;

import com.example.entity.Selfie;

public class FindFriendResponse {
	
	private String friendname;
	private Selfie selfie;
	
	
	

	public FindFriendResponse(String friendname, Selfie selfie) {
		this.friendname = friendname;
		this.selfie = selfie;
	}
	
	
	
	
	public String getFriendname() {
		return friendname;
	}
	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}
	public Selfie getSelfie() {
		return selfie;
	}
	public void setSelfie(Selfie selfie) {
		this.selfie = selfie;
	}

}
