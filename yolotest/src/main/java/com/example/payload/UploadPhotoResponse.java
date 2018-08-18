package com.example.payload;

public class UploadPhotoResponse {
	private String photoName;
	private String photoType;
	private String photoDownloadUri;
	private long size;

	public UploadPhotoResponse(String photoName, String photoType, String photoDownloadUri, long size) {
		super();
		this.photoName = photoName;
		this.photoType = photoType;
		this.photoDownloadUri = photoDownloadUri;
		this.size = size;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}

	public String getPhotoDownloadUri() {
		return photoDownloadUri;
	}

	public void setPhotoDownloadUri(String photoDownloadUri) {
		this.photoDownloadUri = photoDownloadUri;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
