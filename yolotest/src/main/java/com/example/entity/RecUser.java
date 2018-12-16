package com.example.entity;

public class RecUser {
	
	private String PersonId;
	private String ImageSourcePath;
	private String FrameFacePath;
	
	public RecUser(String PersonId, String ImageSourcePath, String FrameFacePath ){
		this.PersonId = PersonId;
		this.ImageSourcePath = ImageSourcePath;
		this.FrameFacePath = FrameFacePath;
	}

	public String getPersonId() {
		return PersonId;
	}

	public void setPersonId(String personId) {
		PersonId = personId;
	}

	public String getImageSourcePath() {
		return ImageSourcePath;
	}

	public void setImageSourcePath(String imageSourcePath) {
		ImageSourcePath = imageSourcePath;
	}

	public String getFrameFacePath() {
		return FrameFacePath;
	}

	public void setFrameFacePath(String frameFacePath) {
		FrameFacePath = frameFacePath;
	}
	
	

	
	
	

}
