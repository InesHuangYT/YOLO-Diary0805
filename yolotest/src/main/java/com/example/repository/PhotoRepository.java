package com.example.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Photo;


@Repository
public interface PhotoRepository  extends JpaRepository<Photo, String> {
	
	Optional<Photo> findByPhotoPath(String photoPath);

}
