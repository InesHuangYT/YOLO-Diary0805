package com.example.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.FetchMode;

import com.example.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;

//FetchType資料
//https://openhome.cc/Gossip/EJB3Gossip/CascadeTypeFetchType.html

//一個相簿有很多日記
@Entity(name = "Album")
@Table(name = "album")
public class Album extends UserDateAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 15)
	private String name;

	// 一個相簿對多張照片
//	@OneToMany(mappedBy = "album", fetch = FetchType.LAZY, orphanRemoval = true)
//	private List<Photo> photo;

	/* 一個相簿可以有很多日記 */
	@OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//	@Size(min = 1)
//	@Fetch(FetchMode.SELECT)
//	@BatchSize(size = 30)
	private List<Diary> diary;
	private String photoUri;

// 11/28
//	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "album_users", joinColumns = { @JoinColumn(name = "album_id") }, inverseJoinColumns = {
			@JoinColumn(name = "users_username") })
	private Set<User> users = new HashSet<>();

	// For deserialisation purposes Album must have a zero-arg constructor.

	public Album() {
	}

	public Album(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Diary> getDiary() {
		return diary;
	}

	public void setDiary(List<Diary> diary) {
		this.diary = diary;
	}

	public String getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}
	

//	public List<AlbumUser> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<AlbumUser> users) {
//		this.users = users;
//	}
//
//	public void addUser(User user) {
//		AlbumUser albumUser = new AlbumUser(this, user);
//		users.add(albumUser);
//		user.getAlbum().add(albumUser);
//	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Album album = (Album) o;
		return Objects.equals(name, album.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
//	public void addDiary(Diary diary) {
//		addDiary(diary);
//		diary.setAlbum(this);
//	}
//
//	public void removeDiary(Diary diary) {
//		removeDiary(diary);
//		diary.setAlbum(null);
//	}

}
