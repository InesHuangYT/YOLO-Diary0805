package com.example.payload;

import java.util.List;

import com.example.entity.Diary;

public class PhotoResponse {
	private String id;

	private byte[] photodata;
	

	public PhotoResponse() {
	}

	public PhotoResponse(String id) {
		this.id = id;
	}

	public PhotoResponse(String id, byte[] photodata) {
		this.id = id;
		this.photodata = photodata;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getPhotodata() {
		return photodata;
	}

	public void setPhotodata(byte[] photodata) {
		this.photodata = photodata;
	}
	


}
