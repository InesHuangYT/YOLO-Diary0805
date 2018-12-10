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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FrameFacePath == null) ? 0 : FrameFacePath.hashCode());
		result = prime * result + ((ImageSourcePath == null) ? 0 : ImageSourcePath.hashCode());
		result = prime * result + ((PersonId == null) ? 0 : PersonId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecUser other = (RecUser) obj;
		if (FrameFacePath == null) {
			if (other.FrameFacePath != null)
				return false;
		} else if (!FrameFacePath.equals(other.FrameFacePath))
			return false;
		if (ImageSourcePath == null) {
			if (other.ImageSourcePath != null)
				return false;
		} else if (!ImageSourcePath.equals(other.ImageSourcePath))
			return false;
		if (PersonId == null) {
			if (other.PersonId != null)
				return false;
		} else if (!PersonId.equals(other.PersonId))
			return false;
		return true;
	}

	
	

}
