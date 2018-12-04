package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Album;
import com.example.entity.AlbumUser;
import com.example.entity.AlbumUserId;
import com.example.entity.PhotoTagUser;
import com.example.entity.PhotoTagUserId;
import com.example.entity.User;

@Repository
public interface AlbumUserRepository extends JpaRepository<AlbumUser, AlbumUserId> {

	Optional<AlbumUser> findByUser(User user);

	Page<AlbumUser> findByUser(User user, Pageable pageable);

	Page<AlbumUser> findByAlbum(Album album, Pageable pageable);

}
