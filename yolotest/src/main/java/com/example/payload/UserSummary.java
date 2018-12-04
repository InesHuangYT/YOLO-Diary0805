package com.example.payload;

public class UserSummary {
	/* 沒有使用UserProfile */

	private String username;
	private String email;

	public UserSummary() {
	}

	public UserSummary(String username) {
		this.username = username;
	}

	public UserSummary(String username, String email) {
		this.username = username;
		this.email = email;
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

}
