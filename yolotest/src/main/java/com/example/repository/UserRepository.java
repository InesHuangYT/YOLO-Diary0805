package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);


	Optional<User> findByUsername(String username);
	
	List<User> findByUsername(List<String> usernames);


	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}