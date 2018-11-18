package com.example.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.example.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
//The best way to map a many-to-many association with extra columns when using JPA and Hibernate
//https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
@Entity//(name = "Photo")
@Table(name = "photo")
public class Photo extends UserDateAudit {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	
	private long batchid;

	private String photoName;

	private String photoType;

	@Lob
//	@JsonIgnore
	private byte[] photodata;
	private String photoUri;
	private String photoPath;
	
	//一個相簿存放多張相片
//	@ManyToOne(fetch = FetchType.EAGER, optional = false)
//	@JoinColumn(name = "album_id", nullable = false)
//	@JsonIgnore
//	private Album album;

	/* 一個日記可以存放很多相片 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "diary_id", nullable = true)
	@JsonIgnore
	private Diary diary;
	
	/* 一個照片可以標記多個使用者 ， 一個使用者可以被多張照片標記 */
//	@OneToMany(mappedBy = "photo", orphanRemoval = true)
//	private List<PhotoTagUser> users = new ArrayList<>();
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "photo_tag_users", joinColumns = @JoinColumn(name = "photo_id"), inverseJoinColumns = @JoinColumn(name = "user_name"))
//	private Set<User> user = new HashSet<>();

	public Photo() {
	}
	
	public Photo(long batchid) {
		this.batchid = batchid;
	}

	public Photo(String id) {
		this.id = id;
	}
	

//	public Photo(Set<User> user) {
//		this.user = user;
//	}

	public Photo(String photoName, String photoType, byte[] photodata) {
		super();
		this.photoName = photoName;
		this.photoType = photoType;
		this.photodata = photodata;
	}

	public Photo(byte[] photodata) {
		super();
		this.photodata = photodata;
	}

	public Photo(String photoName, String photoType, byte[] photodata, Diary diary) {
		this.photoName = photoName;
		this.photoType = photoType;
		this.photodata = photodata;
		this.diary = diary;
	}

	public Photo(String photoName, String photoType, byte[] photodata, String photoUri, Diary diary)  {
		super();
		this.photoName = photoName;
		this.photoType = photoType;
		this.photodata = photodata;
		this.photoUri = photoUri;
		this.diary = diary;
	}

	
//	public Album getAlbum() {
//		return album;
//	}
//
//	public void setAlbum(Album album) {
//		this.album = album;
//	}

	public long getBatchid() {
		return batchid;
	}

	public void setBatchid(long batchid) {
		this.batchid = batchid;
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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

//	public List<PhotoTagUser> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<PhotoTagUser> users) {
//		this.users = users;
//	}

//	public void addUser(User user,Long diaryId, String facepath, byte[] facedata, String uri) {
//		PhotoTagUser photoTagUser = new PhotoTagUser(this, user,diaryId,facepath, facedata, uri);
//		users.add(photoTagUser);
//		user.getPhoto().add(photoTagUser);
//	}
//	public void addUser(User user,Long diaryId, String facepath) {
//		PhotoTagUser photoTagUser = new PhotoTagUser(this, user,diaryId,facepath);
//		users.add(photoTagUser);
//		user.getPhoto().add(photoTagUser);
//	}


//	public void removeUser(User user) {
//		for (Iterator<PhotoTagUser> iterator = users.iterator(); iterator.hasNext();) {
//			PhotoTagUser photoTagUser = iterator.next();
//
//			if (photoTagUser.getPhoto().equals(this) && photoTagUser.getUser().equals(user)) {
//				iterator.remove();
//				photoTagUser.getUser().getPhoto().remove(photoTagUser);
//				photoTagUser.setPhoto(null);
//				photoTagUser.setUser(null);
//			}
//		}
//	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Photo photo = (Photo) o;
		return Objects.equals(photoName, photo.photoName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(photoName);
	}
//	public Set<User> getUser() {
//		return user;
//	}
//
//	public void setUser(Set<User> user) {
//		this.user = user;
//	}

}
