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
	
    //使用者改成上傳一張
	/* 一個使用者可以上傳多張頭貼 */
	// @OnDelete(action = OnDeleteAction.CASCADE)
//	@OneToMany(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "username", nullable = false)
//	@JsonIgnore
//	private User user;
	
	@OneToOne(mappedBy = "selfie")
	private User user;

	public Selfie() {
	}

	
	
	public Selfie(String selfieName, String selfieType, byte[] selfiedata) {
		this.selfieName = selfieName;
		this.selfieType = selfieType;
		this.selfiedata = selfiedata;
	}


	public Selfie(String selfieName, String selfieType, byte[] selfiedata, String selfieUri) {
		this.selfieName = selfieName;
		this.selfieType = selfieType;
		this.selfiedata = selfiedata;
		this.selfieUri = selfieUri;
		
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
