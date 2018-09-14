package com.example.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity(name = "PhotoTagUser")
@Table(name = "photo_tag_users")
public class PhotoTagUser {
	
	//private String batchid =  
	
	@EmbeddedId
	private PhotoTagUserId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("photoId")
	private Photo photo;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User users;

	private Long diaryId;

	public PhotoTagUser() {
	}

	public PhotoTagUser(Photo photo, User user) {
		this.photo = photo;
		this.users = user;
		this.id = new PhotoTagUserId(photo.getId(),user.getUsername());
	}
	
	
	public PhotoTagUser(Photo photo, User users, Long diaryId) {
		this.photo = photo;
		this.users = users;
		this.diaryId = diaryId;
		this.id = new PhotoTagUserId(photo.getId(),users.getUsername());

	}

	public PhotoTagUserId getId() {
		return id;
	}

	public void setId(PhotoTagUserId id) {
		this.id = id;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public User getUser() {
		return users;
	}

	public void setUser(User user) {
		this.users = user;
	}

	public Long getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(Long diaryId) {
		this.diaryId = diaryId;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        PhotoTagUser that = (PhotoTagUser) o;
        return Objects.equals(photo, that.photo) &&
               Objects.equals(users, that.users);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(photo, users);
    }

}
