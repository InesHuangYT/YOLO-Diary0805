package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.PhotoTagUser;
import com.example.entity.PhotoTagUserId;



@Repository
public interface PhotoTagUserRepository extends JpaRepository<PhotoTagUser, PhotoTagUserId>{

	
}
