package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.example.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "photo")
public class Photo extends UserDateAudit {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String photoName;

	private String photoType;

	@Lob
	private byte[] photodata;
	private String photoUri;

	/* 一個日記可以存放很多相片 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "diary_id", nullable = false)
	@JsonIgnore
	private Diary diary;

	public Photo() {
	}

	public Photo(String photoName, String photoType, byte[] photodata) {
		super();
		this.photoName = photoName;
		this.photoType = photoType;
		this.photodata = photodata;
	}

	public Photo(String photoName, String photoType, byte[] photodata, Diary diary) {
		this.photoName = photoName;
		this.photoType = photoType;
		this.photodata = photodata;
		this.diary = diary;
	}
	

	public Photo(String photoName, String photoType, byte[] photodata, String photoUri, Diary diary) {
		super();
		this.photoName = photoName;
		this.photoType = photoType;
		this.photodata = photodata;
		this.photoUri = photoUri;
		this.diary = diary;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}

	public byte[] getPhotodata() {
		return photodata;
	}

	public void setPhotodata(byte[] photodata) {
		this.photodata = photodata;
	}

	public Diary getDiary() {
		return diary;
	}

	public void setDiary(Diary diary) {
		this.diary = diary;
	}

	public String getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

}
