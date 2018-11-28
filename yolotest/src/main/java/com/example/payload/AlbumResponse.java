package com.example.payload;

import java.util.List;

import com.example.entity.Diary;

public class AlbumResponse {

	private Long id;
	private String name;
	private List<Diary> diaries;
	private String photoCover;
//	private List<DiaryResponse> diaries;

	public AlbumResponse() {
	}

	public AlbumResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public AlbumResponse(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	
}
