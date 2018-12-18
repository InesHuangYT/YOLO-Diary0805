package com.example.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {

	Optional<Album> findByName(String albumName);

	Optional<Album> findByCreatedAt(Instant time);

	List<Album> findByNameIn(String names);

	Page<Album> findByCreatedBy(String userId, Pageable pageable);
}
