//package com.example.entity;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//
//@Embeddable
//
//public class AlbumUserId implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 4619155091561329687L;
//	@Column(name = "album_id")
//	private Long albumId;
//	@Column(name = "users_username")
//	private String username;
//
//	public AlbumUserId() {
//
//	}
//
//	public AlbumUserId(Long albumId, String username) {
//		this.albumId = albumId;
//		this.username = username;
//	}
//
//	public Long getAlbumId() {
//		return albumId;
//	}
//
//	public void setAlbumId(Long albumId) {
//		this.albumId = albumId;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//
//		if (o == null || getClass() != o.getClass())
//			return false;
//
//		AlbumUserId that = (AlbumUserId) o;
//		return Objects.equals(albumId, that.albumId) && Objects.equals(username, that.username);
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(albumId, username);
//	}
//
//}
