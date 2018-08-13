package com.example.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Selfie;


@Repository
public interface SelfieRepository  extends JpaRepository<Selfie, String> {

}
