package com.example.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Album;

import antlr.collections.List;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
	Optional<Album> findByName(String albumName);
	Page<Album> findByCreatedBy(String userId, Pageable pageable);
}
