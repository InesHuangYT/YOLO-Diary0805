package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.User;
import com.example.entity.UserFriend;
import com.example.entity.UserFriendId;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, UserFriendId>{

	
	Optional<UserFriend> findByUser(User anyUser);
	
	Optional<UserFriend> deleteByUser(User anyUser);
	
	boolean existsById(UserFriendId id);

	
	
	
}
