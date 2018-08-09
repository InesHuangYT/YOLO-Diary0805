package com.example.entity;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_friends")
@IdClass(UserFriendId.class)
public class UserFriend {
	    @Id
	    @ManyToOne
	    @JoinColumn(name = "user_name", referencedColumnName = "username")
	    private User user;

	    @Id
	    @ManyToOne
	    @JoinColumn(name = "friend_name", referencedColumnName = "username")
	    private User friend;

	    @Column(name = "is_confirmed")
	    private boolean isConfirmed;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public User getFriend() {
			return friend;
		}
		
		public void setFriend(User friend) {
			this.friend = friend;
			
		}

		public boolean isConfirmed() {
			return isConfirmed;
		}

		public void setConfirmed(boolean isConfirmed) {
			this.isConfirmed = isConfirmed;
		}

	

		
	    
	    
	    
	    
}

