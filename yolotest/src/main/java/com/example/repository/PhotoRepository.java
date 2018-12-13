package com.example.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Diary;
import com.example.entity.Photo;

import antlr.collections.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

	Optional<Photo> findByPhotoPath(String photoPath);

	Optional<Photo> findById(String photoid);

	Page<Photo> findByAlbumId(String albumId, Pageable pageable);

	Optional<Photo> findByDiary(Diary diary);

	Page<Photo> findByDiaryId(String diaryId, Pageable pageable);

	Page<Photo> findByDiary(Diary diary, Pageable pageable);

}
