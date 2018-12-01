package com.example.payload;

import java.util.List;

import com.example.entity.Diary;

public class AlbumUserResponse {

	private String albumId;
	private String username;
	
	public AlbumUserResponse() {
	}

	public AlbumUserResponse(String albumId, String username) {
		this.albumId = albumId;
		this.username = username;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
}