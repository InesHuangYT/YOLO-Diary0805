package com.example.entity;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "selfie")
public class Selfie {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String selfieName;

	private String selfieType;

	@Lob
	private byte[] selfiedata;
	
	private String selfieUri;
	private String selfiePath;
	

	/* 一個使用者可以上傳多張頭貼 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "username", nullable = false)
	// @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;

	public Selfie() {
	}

	public Selfie(User user) {
		this.user = user;
	}

	public Selfie(String selfieName, String selfieType, byte[] selfiedata) {
		this.selfieName = selfieName;
		this.selfieType = selfieType;
		this.selfiedata = selfiedata;
	}
	

	public Selfie(String selfieName, String selfieType, byte[] selfiedata, User user) {
		this.selfieName = selfieName;
		this.selfieType = selfieType;
		this.selfiedata = selfiedata;
		this.user = user;
	}

	public Selfie(String selfieName, String selfieType, byte[] selfiedata, String selfieUri, User user) {
		this.selfieName = selfieName;
		this.selfieType = selfieType;
		this.selfiedata = selfiedata;
		this.selfieUri = selfieUri;
		this.user = user;
	}

	public String getSelfieName() {
		return selfieName;
	}

	public void setSelfieName(String selfieName) {
		this.selfieName = selfieName;
	}

	public String getSelfieType() {
		return selfieType;
	}

	public void setSelfieType(String selfieType) {
		this.selfieType = selfieType;
	}

	public byte[] getSelfiedata() {
		return selfiedata;
	}

	public void setSelfiedata(byte[] selfiedata) {
		this.selfiedata = selfiedata;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSelfieUri() {
		return selfieUri;
	}

	public void setSelfieUri(String selfieUri) {
		this.selfieUri = selfieUri;
	}

	public String getSelfiePath() {
		return selfiePath;
	}

	public void setSelfiePath(String selfiePath) {
		this.selfiePath = selfiePath;
	}
	

}
