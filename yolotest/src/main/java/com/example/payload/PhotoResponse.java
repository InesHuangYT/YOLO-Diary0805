package com.example.payload;

import java.util.List;

import com.example.entity.Diary;

public class PhotoResponse {
	private String id;

	private String photoUri;
	

	public PhotoResponse() {
	}

	public PhotoResponse(String id) {
		this.id = id;
	}

	public PhotoResponse(String id, String photoUri) {
		this.id = id;
		this.photoUri = photoUri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String photoUri() {
		return photoUri;
	}

	public void setphotoUri(String photoUri) {
		this.photoUri = photoUri;
	}
	


}
