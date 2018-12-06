package com.example.payload;

public class UserSummary {
	/* 沒有使用UserProfile */

	private String username;
	private String email;
	private byte[] photodata;

	public UserSummary() {
	}

	public UserSummary(String username) {
		this.username = username;
	}

	public UserSummary(String username, String email) {
		this.username = username;
		this.email = email;
	}
	

	public UserSummary(String username, String email, byte[] photodata) {
		this.username = username;
		this.email = email;
		this.photodata = photodata;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPhotodata() {
		return photodata;
	}

	public void setPhotodata(byte[] photodata) {
		this.photodata = photodata;
	}
	

}
