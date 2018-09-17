package com.example.entity;

import java.util.HashSet;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
//一篇日記屬於一個相簿
@Entity//(name = "Diary")
@Table(name = "diary")
public class Diary extends UserDateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 5)
	private String text;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "album_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore //不加才能response album json
	private Album album;
	

	/*一個日記可以有很多張照片*/
	@OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Photo> photo;
	
	
	public Diary() {
	}

	public Diary(Long id) {
		super();
		this.id = id;
	}

	public Diary(@NotBlank @Size(min = 5) String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	
	

	
//Object類中默認的實現方式是 : return this == obj 。
//那就是說，只有this 和 obj引用同一個對象，才會返回true。



	public List<Photo> getPhoto() {
		return photo;
	}

	public void setPhoto(List<Photo> photo) {
		this.photo = photo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Diary diary = (Diary) o;
		return Objects.equals(id, diary.id);
	}
//直接傳回其物件的記憶體位址
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}



//@OneToMany(mappedBy = "diary",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//private List<UserTagDiary> userTagDiary;
//@ManyToMany(fetch = FetchType.LAZY)
//@JoinTable(name = "users_diaries", joinColumns = @JoinColumn(name = "diary_id"), inverseJoinColumns = @JoinColumn(name = "username"))
//private Set<User> users = new HashSet<>();

//@ManyToMany(mappedBy = "diaries")
//private Set<User> users = new HashSet<>();
