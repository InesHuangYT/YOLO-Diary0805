package com.example.payload;

import java.time.Instant;
import java.util.List;

import com.example.entity.Diary;

public class AlbumResponse {

	private String id;
	private String name;
	private String createdAt;
	private List<Diary> diaries;
	private String photoCover;
//	private List<DiaryResponse> diaries;

	public AlbumResponse() {
	}

	public AlbumResponse(String id, String name) {
		this.id = id;
		this.name = name;
	}
	

	public AlbumResponse(String id, String name, String createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public AlbumResponse(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Diary> getDiaries() {
		return diaries;
	}

	public void setDiaries(List<Diary> diary) {
		this.diaries = diary;
	}

	public String getPhotoCover() {
		return photoCover;
	}

	public void setPhotoCover(String photoCover) {
		this.photoCover = photoCover;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	

	
}
