package com.example.payload;

public class DeleteTagRequest {
 
	private String diaryId;
	private String albumId;
	private String username;
	
	
	
	public DeleteTagRequest(String diaryId, String albumId, String username) {
		super();
		this.diaryId = diaryId;
		this.albumId = albumId;
		this.username = username;
	}
	
	
	public String getDiaryId() {
		return diaryId;
	}
	public void setDiaryId(String diaryId) {
		this.diaryId = diaryId;
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
