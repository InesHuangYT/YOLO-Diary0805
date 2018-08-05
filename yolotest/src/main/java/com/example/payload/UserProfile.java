package com.example.payload;

import java.time.Instant;

public class UserProfile {
	private String username;
	private Instant joinedAt;

	public UserProfile(String username, Instant joinedAt) {
		this.username = username;
		this.joinedAt = joinedAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Instant getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Instant joinedAt) {
		this.joinedAt = joinedAt;
	}

}
