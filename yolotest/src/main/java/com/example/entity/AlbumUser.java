package com.example.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.entity.audit.UserDateAudit;

@Entity(name = "AlbumUser")
@Table(name = "album_users")
@IdClass(AlbumUserId.class)

public class AlbumUser extends UserDateAudit {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id", referencedColumnName = "id")
	private Album album;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
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
	

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		AlbumUser that = (AlbumUser) o;
		return Objects.equals(album, that.album) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(album, user);
	}

}
