package com.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Notice;



@Repository
public interface NoticeRepository  extends JpaRepository<Notice, Long> {
	
	

}
