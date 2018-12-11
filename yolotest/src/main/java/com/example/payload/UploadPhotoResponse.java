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




}
