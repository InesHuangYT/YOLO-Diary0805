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
import org.hibernate.annotations.GenericGenerator;

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
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

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
//	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//	@JoinTable(name = "album_users", joinColumns = { @JoinColumn(name = "album_id") }, inverseJoinColumns = {
//			@JoinColumn(name = "users_username") })
//	private Set<User> users = new HashSet<>();

	// For deserialisation purposes Album must have a zero-arg constructor.

	public Album() {
	}

	public Album(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
//11/28
//	public Set<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (diary == null) {
			if (other.diary != null)
				return false;
		} else if (!diary.equals(other.diary))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (photoUri == null) {
			if (other.photoUri != null)
				return false;
		} else if (!photoUri.equals(other.photoUri))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diary == null) ? 0 : diary.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((photoUri == null) ? 0 : photoUri.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Album [id=" + id + ", name=" + name + ", diary=" + diary + ", photoUri=" + photoUri + "]";
	}



}
