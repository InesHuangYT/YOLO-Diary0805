package com.example.payload;

public class UploadPhotoResponse {
	private String facePersonId;
	private String faceData;
	private String photoCover;
	private long size;
	
	public UploadPhotoResponse(String facePersonId, String faceData, String photoCover, long size) {
		super();
		this.facePersonId = facePersonId;
		this.faceData = faceData;
		this.photoCover = photoCover;
		this.size = size;
	}

	public String getFacePersonId() {
		return facePersonId;
	}

	public void setFacePersonId(String facePersonId) {
		this.facePersonId = facePersonId;
	}

	public String getFaceData() {
		return faceData;
	}

	public void setFaceData(String faceData) {
		this.faceData = faceData;
	}

	public String getPhotoCover() {
		return photoCover;
	}

	public void setPhotoCover(String photoCover) {
		this.photoCover = photoCover;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	

	

}
