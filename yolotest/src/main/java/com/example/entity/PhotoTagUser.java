package com.example.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

//前端要拿這個資料表的資料->找同DIARY ID 下的使用者->對應的人臉圖-顯示到前端
@Entity(name = "PhotoTagUser")
@Table(name = "photo_tag_users")
@IdClass(PhotoTagUserId.class)
public class PhotoTagUser {


	@Id
    @ManyToOne
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
	private Photo photo;

	@Id
    @ManyToOne
    @JoinColumn(name = "users_username", referencedColumnName = "username")
	private User user;

	private Long diaryId;

	private String face_path;
    
	@Lob
	private byte[] face_data;

	private String face_uri;

	
	public PhotoTagUser() {
		
	}
	
	public PhotoTagUser(Photo photo, User user, Long diaryId, String face_path, byte[] face_data, String face_uri) {
		
		this.photo = photo;
		this.user = user;
		this.diaryId = diaryId;
		this.face_path = face_path;
		this.face_data = face_data;
		this.face_uri = face_uri;
	}

	
	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(Long diaryId) {
		this.diaryId = diaryId;
	}

	public String getFace_path() {
		return face_path;
	}

	public void setFace_path(String face_path) {
		this.face_path = face_path;
	}

	public byte[] getFace_data() {
		return face_data;
	}

	public void setFace_data(byte[] face_data) {
		this.face_data = face_data;
	}

	public String getFace_uri() {
		return face_uri;
	}

	public void setFace_uri(String face_uri) {
		this.face_uri = face_uri;
	}


	
}
