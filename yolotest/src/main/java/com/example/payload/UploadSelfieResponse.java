package com.example.payload;

public class UploadSelfieResponse {
	private String selfieName;
	private String selfieType;
	private String selfieDownloadUri;
	private long size;

	public UploadSelfieResponse(String selfieName, String selfieType, String selfieDownloadUri, long size) {
		this.selfieName = selfieName;
		this.selfieType = selfieType;
		this.selfieDownloadUri = selfieDownloadUri;
		this.size = size;
	}

	public String getSelfieName() {
		return selfieName;
	}

	public void setSelfieName(String selfieName) {
		this.selfieName = selfieName;
	}

	public String getSelfieType() {
		return selfieType;
	}

	public void setSelfieType(String selfieType) {
		this.selfieType = selfieType;
	}

	public String getSelfieDownloadUri() {
		return selfieDownloadUri;
	}

	public void setSelfieDownloadUri(String selfieDownloadUri) {
		this.selfieDownloadUri = selfieDownloadUri;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
