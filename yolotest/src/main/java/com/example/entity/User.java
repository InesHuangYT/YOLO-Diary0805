package com.example.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.entity.audit.DateAudit;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }),
		@UniqueConstraint(columnNames = { "email" }) })
public class User extends DateAudit {

	@Id
	@NotBlank
	private String username;// 帳號

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;
	
	/*一個使用者可以上傳多張頭貼*/
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Selfie> selfie ;
	
	private boolean enabled;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();


	@ManyToMany(fetch = FetchType.LAZY,  mappedBy = "tag_user")
	private Set<Diary> tag = new HashSet<>();

	public User() {
	}
	

	public User(boolean enabled) {
		super();
		this.enabled = false;
	}


	public User(@NotBlank String username) {
		this.username = username;
	}

	public User(@NotBlank String username, @NotBlank @Email String email, @NotBlank String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public List<Selfie> getSelfie() {
		return selfie;
	}


	public void setSelfie(List<Selfie> selfie) {
		this.selfie = selfie;
	}


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public Set<Diary> getTag() {
		return tag;
	}


	public void setTag(Set<Diary> tag) {
		this.tag = tag;
	}
	


}

//@OneToMany(mappedBy = "users",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//private List<UserTagDiary> userTagDiary;

//@ManyToMany(fetch = FetchType.LAZY)
//@JoinTable(name = "users_diaries", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "diary_id"))
//private Set<Diary> diaries = new HashSet<>();

// http://blog.jbaysolutions.com/2013/06/06/jpa-2-tutorial-many-to-many-with-self-2/
// 自己對自己關聯
/*
 * @JoinTable(name = "users_friends", joinColumns = {
 * 
 * @JoinColumn(name = "user_name", referencedColumnName = "username", nullable =
 * false)}, , @JoinColumn(name = "status", nullable = false) inverseJoinColumns
 * = {
 * 
 * @JoinColumn(name = "friend_name", referencedColumnName = "username", nullable
 * = false)})
 * 
 * @ManyToMany private Set<User> friends = new HashSet<User>();
 */
