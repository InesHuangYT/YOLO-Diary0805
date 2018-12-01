package com.example.payload;

import java.time.Instant;

public class DiaryResponse {
	private String id;
	private String text;
	private String createdBy;

//	private UserSummary createdBy;
	private Instant creationDateTime;
	private String albumId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

//	public UserSummary getCreatedBy() {
//		return createdBy;
//	}
//
//	public void setCreatedBy(UserSummary createdBy) {
//		this.createdBy = createdBy;
//	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Instant creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

}
