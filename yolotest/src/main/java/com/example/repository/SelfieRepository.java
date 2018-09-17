package com.example.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Selfie;
import com.example.entity.User;


@Repository
public interface SelfieRepository  extends JpaRepository<Selfie, String> {
	Boolean existsByUser(Optional<User> user);
}
