package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Album;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
	Optional<Album> findByName(String albumName);
	
}
