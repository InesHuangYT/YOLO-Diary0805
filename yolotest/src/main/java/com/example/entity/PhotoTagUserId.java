package com.example.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PhotoTagUserId implements Serializable {
	@Column(name = "photo_id")
	private String photoId;
	@Column(name = "users_username")
	private String userId;

	public PhotoTagUserId(String photoId, String userId) {
		this.photoId = photoId;
		this.userId = userId;
	}

	public PhotoTagUserId() {
	}


	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass()) 
            return false;
 
        PhotoTagUserId that = (PhotoTagUserId) o;
        return Objects.equals(photoId, that.photoId) && 
               Objects.equals(userId, that.userId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(photoId, userId);
    }

}
