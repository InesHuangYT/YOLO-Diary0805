package com.example.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity(name = "AlbumUser")
@Table(name = "album_users")
@IdClass(AlbumUserId.class)

public class AlbumUser{

	@Id
	@ManyToOne
	@JoinColumn(name = "album_id", referencedColumnName = "id")
	private Album album;
	@Id
	@ManyToOne
	@JoinColumn(name = "users_username", referencedColumnName = "username")
	private User user;

	public AlbumUser() {

	}

	public AlbumUser(Album album, User user) {

		this.album = album;
		this.user = user;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
