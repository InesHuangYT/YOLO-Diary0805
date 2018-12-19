package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Diary;

@Repository

public interface DiaryRepository extends JpaRepository<Diary, String> {

	Optional<Diary> findById(String diaryId);

	Optional<Diary> findByPhotoId(String photoId);
	
	Optional<Diary> findByAlbumIdAndCreatedBy(String albumId, String username);


	Page<Diary> findByCreatedBy(String userId, Pageable pageable);

	Page<Diary> findByAlbumId(String albumId, Pageable pageable);

//    long countByCreatedBy(Long userId);
	List<Diary> findByIdIn(List<String> diaryIds);

	List<Diary> findByIdIn(List<String> diaryIds, Sort sort);

}
