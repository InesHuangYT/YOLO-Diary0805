package com.example.payload;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
 * write the APIs to create a diary, 
 * get all diaries, get a user’s profile, 
 * get diaries created by a user etc.
 *  in diary,we have many pictures and text,besides,belong to one album
 */

public class AlbumRequest {
	@NotBlank
	@Size(max = 15)
	private String name;

	@NotNull
//	@Size(min = 1)
	@Valid
	private List<DiaryRequest> diaries;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DiaryRequest> getDiaries() {
		return diaries;
	}

	public void setDiaries(List<DiaryRequest> diaries) {
		this.diaries = diaries;
	}

}
