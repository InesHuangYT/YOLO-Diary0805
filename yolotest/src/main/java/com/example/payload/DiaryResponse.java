package com.example.payload;

import java.time.Instant;

public class DiaryResponse {
	private Long id;
	private String text;
	private String createdBy;

//	private UserSummary createdBy;
	private Instant creationDateTime;
	private Long albumId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

}
