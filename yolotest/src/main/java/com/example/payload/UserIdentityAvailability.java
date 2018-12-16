package com.example.payload;

public class UserIdentityAvailability {
	private Boolean available;
	private String username;

	public UserIdentityAvailability(Boolean available, String username) {
		super();
		this.available = available;
		this.username = username;
	}

	public UserIdentityAvailability(Boolean available) {
		this.available = available;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
