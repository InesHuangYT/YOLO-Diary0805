package com.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.engine.controller.GetResult;
import com.example.engine.entity.Face;
import com.example.entity.Diary;
import com.example.entity.Photo;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.repository.DiaryRepository;
import com.example.repository.PhotoRepository;
import com.example.repository.UserRepository;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/engineTag")

public class EngineAndHandTagUserController {
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	static GetResult getResult;


	public String findPhotoIdByPhotoPath(String imageSourcePath) {
		return photoRepository.findByPhotoPath(imageSourcePath).map(photo -> {
			String photoId = photo.getId();
			return photoId;
		}).orElseThrow(() -> new BadRequestException("PhotoPath " + imageSourcePath + " not found"));
	}

	public String findUsernameByPersonId(String username) {
		return userRepository.findByUsername(username).map(user -> {
			return username;
		}).orElseThrow(() -> new BadRequestException("Username " + username + " not found"));
	}

	// 引擎自動標記
	// @PostMapping("/{personId}")
	public Photo engineTag(@PathVariable(value = "personId") String personId,
			@RequestParam("imageSourcePath") String imageSourcePath) {
		String photoId = findPhotoIdByPhotoPath(imageSourcePath);
		String username = findUsernameByPersonId(personId);

		return photoRepository.findById(photoId).map(photo -> {
			User user = new User(username);
			photo.getUser().add(user);

			return photoRepository.save(photo);
		}).orElseThrow(() -> new BadRequestException("PhotoId " + photoId + " not found"));

	}

	// 手動標記 一張照片標記很多個人
	public Photo handTag(@PathVariable(value = "photoId") String photoId, @RequestParam("username") String personId) {
		String username = findUsernameByPersonId(personId);
		return photoRepository.findById(photoId).map(photo -> {
			User user = new User(username);
			photo.getUser().add(user);
			return photoRepository.save(photo);
		}).orElseThrow(() -> new BadRequestException("PhotoId " + photoId + " not found"));

	}

	@PostMapping("/{photoId}")
	public List<String> handTags(@PathVariable(value = "photoId") String photoId,
			@RequestParam("username") String[] username) {
		if (username != null && username.length > 0) {
			for (int i = 0; i < username.length; i++) {
				String user = username[i];
				handTag(photoId, user);
			}
		}

		return Arrays.asList(username);
	}

}
