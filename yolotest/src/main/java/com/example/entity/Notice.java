package com.example.entity;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.entity.audit.DateAudit;


/*通知：前端按下好友申請，即儲存好友通知至通知資料庫，可用findbyuser 列出所有通知並呈現到通知頁面上
 當對方按下同意好友，便儲存接受通知到通知資料庫，並設userid，下次用戶登入會顯示紅色通知數，每點一次紅色通知，全數通知isread設為1*/

@Entity
@Table(name = "notice")
public class Notice extends DateAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String message;
	
	private boolean isRead;
	
	
//	@OneToMany
//    @JoinColumn(name = "sendby", referencedColumnName = "username")
//    private User sender;
	
	@ManyToOne
    @JoinColumn(name = "receive", referencedColumnName = "username")
    private User receive;
	
	
	
	
public Notice(){}
	
	public Notice(String message,User receive){
		this.message = message;
		this.receive = receive;
//		this.sender = sender;
		this.isRead = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

//	public User getSender() {
//		return sender;
//	}
//
//	public void setSender(User sender) {
//		this.sender = sender;
//	}
//
//	public User getReceive() {
//		return receive;
//	}

	public void setReceive(User receive) {
		this.receive = receive;
	}

	
	
	
	
	
	

}
