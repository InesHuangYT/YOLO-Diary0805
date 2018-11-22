package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.AlbumUser;
import com.example.entity.AlbumUserId;
import com.example.entity.PhotoTagUser;
import com.example.entity.PhotoTagUserId;
import com.example.entity.User;

@Repository
public interface AlbumUserRepository extends JpaRepository<AlbumUser, AlbumUserId> {

	Optional<AlbumUser> findByUser(User user);

	
}
