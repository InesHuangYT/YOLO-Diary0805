package com.example.entity;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.FetchMode;

import com.example.entity.audit.UserDateAudit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;

//FetchType資料
//https://openhome.cc/Gossip/EJB3Gossip/CascadeTypeFetchType.html


//一個相簿有很多日記
@Entity(name = "Album")
@Table(name = "album")
public class Album extends UserDateAudit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 15)
	private String name;
	
	//一個相簿對多張照片
//	@OneToMany(mappedBy = "album", fetch = FetchType.LAZY, orphanRemoval = true)
//	private List<Photo> photo;

	/*一個相簿可以有很多日記*/
	@OneToMany(mappedBy = "album",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//	@Size(min = 1)
//	@Fetch(FetchMode.SELECT)
//	@BatchSize(size = 30)
	private List<Diary> diary ;
	
	
	//For deserialisation purposes Album must have a zero-arg constructor.
	
	public Album() {
	}

//	public List<Photo> getPhoto() {
//		return photo;
//	}
//
//	public void setPhoto(List<Photo> photo) {
//		this.photo = photo;
//	}

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
