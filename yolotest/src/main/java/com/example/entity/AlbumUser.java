//package com.example.entity;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//import javax.persistence.Embeddable;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.MapsId;
//import javax.persistence.Table;
//
//@Entity(name = "AlbumUser")
//@Table(name = "album_users")
//@IdClass(AlbumUserId.class)
//
//public class AlbumUser {
//
//	@EmbeddedId
//	private AlbumUserId id;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@MapsId("albumId")
//	private Album album;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@MapsId("username")
//	private User user;
//
//	public AlbumUser() {
//
//	}
//
//	public AlbumUser(Album album, User user) {
//		this.album = album;
//		this.user = user;
//		this.id = new AlbumUserId(album.getId(), user.getUsername());
//
//	}
//
//	public AlbumUserId getId() {
//		return id;
//	}
//
//	public void setId(AlbumUserId id) {
//		this.id = id;
//	}
//
//	public Album getAlbum() {
//		return album;
//	}
//
//	public void setAlbum(Album album) {
//		this.album = album;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//	
//	@Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass())
//            return false;
//
//        AlbumUser that = (AlbumUser) o;
//		return Objects.equals(album, that.album) &&
//               Objects.equals(user, that.user);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(album, user);
//    }
//
//}
