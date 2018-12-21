package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Photo;
import com.example.entity.PhotoTagUser;
import com.example.entity.PhotoTagUserId;
import com.example.entity.User;

@Repository
public interface PhotoTagUserRepository extends JpaRepository<PhotoTagUser, PhotoTagUserId> {

	Optional<PhotoTagUser> findByFaceRandom(String faceRandom);
	Optional<PhotoTagUser> findByUser(User user);
	Optional<PhotoTagUser> findByUserAndDiaryId(User user, String diaryId);

	Page<PhotoTagUser> findByDiaryId(String diaryId, Pageable pageable);

}
