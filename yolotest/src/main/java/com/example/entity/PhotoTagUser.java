package com.example.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


//前端要拿這個資料表的資料->找同DIARY ID 下的使用者->對應的人臉圖-顯示到前端
@Entity(name = "PhotoTagUser")
@Table(name = "photo_tag_users")
public class PhotoTagUser {
	
	@EmbeddedId
	private PhotoTagUserId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("photoId")
	private Photo photo;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User users;

	private Long diaryId;
	
	private String facepath;

	public PhotoTagUser() {
	}

	public PhotoTagUser(Photo photo, User user) {
		this.photo = photo;
		this.users = user;
		this.id = new PhotoTagUserId(photo.getId(),user.getUsername());
		
	}
	
	
	public PhotoTagUser(Photo photo, User users, Long diaryId, String facepath) {
		this.photo = photo;
		this.users = users;
		this.diaryId = diaryId;
		this.id = new PhotoTagUserId(photo.getId(),users.getUsername());
		this.facepath = facepath;
		

	}

	
	
	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	public String getFacepath() {
		return facepath;
	}

	public void setFacepath(String facepath) {
		this.facepath = facepath;
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
