package com.example.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Album;

import antlr.collections.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {

    
	Optional<Album> findByName(String albumName);
	Optional<Album> findByCreatedAt(Instant time);


	Page<Album> findByCreatedBy(String userId, Pageable pageable);
}
