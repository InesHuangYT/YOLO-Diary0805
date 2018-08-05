package com.example.payload;

public class UserSummary {
	/*沒有使用UserProfile*/

	private String username;

	public UserSummary(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
