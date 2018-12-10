package com.example.payload;

public class UploadPhotoResponse {
	private String facePersonId;
	private String faceData;
	private String photoCoverUri;
	
	
	public UploadPhotoResponse(String facePersonId, String faceData, String photoCoverUri) {
		super();
		this.facePersonId = facePersonId;
		this.faceData = faceData;
		this.photoCoverUri = photoCoverUri;
		
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

	public String getPhotoCoverUri() {
		return photoCoverUri;
	}

	public void setPhotoCover(String photoCoverUri) {
		this.photoCoverUri = photoCoverUri;
	}


	
	

	

}
