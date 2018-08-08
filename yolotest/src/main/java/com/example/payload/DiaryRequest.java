//package com.example.payload;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//
//import com.example.entity.Album;
//
///*
// * write the APIs to create a diary, 
// * get all diaries, get a user’s profile, 
// * get diaries created by a user etc.
// *  in diary,we have many pictures and text,besides,belong to one album
// */
//public class DiaryRequest {
//	@NotBlank
//	@Size(min = 5)
//	private String text;
//
//	@NotNull
//	
//	private Album album;
//
//	public String getText() {
//		return text;
//	}
//
//	public void setText(String text) {
//		this.text = text;
//	}
//
//	public Album getAlbum() {
//		return album;
//	}
//
//	public void setAlbum(Album album) {
//		this.album = album;
//	}
//
//}
