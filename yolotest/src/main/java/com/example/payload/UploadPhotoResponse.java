package com.example.payload;

import java.util.List;

public class UploadPhotoResponse {
	private List<SaveFaceResponse> sfr;
	private List<NotFoundFaceResponse> nffr;
//	private String facePersonId;
//	private byte[] faceData;
	private String photoCoverUri;
	//private long size;
	
	
	
	public UploadPhotoResponse(List<SaveFaceResponse> sfr, List<NotFoundFaceResponse> nffr, String photoCoverUri) {
		super();
		this.sfr = sfr;
		this.nffr = nffr;
		this.photoCoverUri = photoCoverUri;
	}
	
//	public UploadPhotoResponse(String facePersonId, String faceData, String photoCoverUri, long size) {
//		super();
//		this.facePersonId = facePersonId;
//		this.faceData = faceData;
//		this.photoCoverUri = photoCoverUri;
//		this.size = size;
//	}
	
//	public UploadPhotoResponse(String facePersonId,  byte[] faceData, String photoCoverUri) {
//		super();
//		this.facePersonId = facePersonId;
//		this.faceData = faceData;
//		this.photoCoverUri = photoCoverUri;
//		
//	}
	

//	public String getFacePersonId() {
//		return facePersonId;
//	}
//
//	public void setFacePersonId(String facePersonId) {
//		this.facePersonId = facePersonId;
//	}
//
//	public byte[] getFaceData() {
//		return faceData;
//	}
//
//	public void setFaceData(byte[] faceData) {
//		this.faceData = faceData;
//	}

	

	public String getPhotoCoverUri() {
		return photoCoverUri;
	}


	public List<SaveFaceResponse> getSfr() {
		return sfr;
	}

	public void setSfr(List<SaveFaceResponse> sfr) {
		this.sfr = sfr;
	}

	public void setPhotoCover(String photoCoverUri) {
		this.photoCoverUri = photoCoverUri;
	}

	public List<NotFoundFaceResponse> getNffr() {
		return nffr;
	}

	public void setNffr(List<NotFoundFaceResponse> nffr) {
		this.nffr = nffr;
	}

//	public long getSize() {
//		return size;
//	}
//
//	public void setSize(long size) {
//		this.size = size;
//	}
//	
//	

	

}
