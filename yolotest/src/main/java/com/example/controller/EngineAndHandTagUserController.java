package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.engine.controller.GetResult;
import com.example.engine.entity.Face;
import com.example.entity.Diary;
import com.example.entity.Photo;
import com.example.entity.PhotoTagUser;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.repository.DiaryRepository;
import com.example.repository.PhotoRepository;
import com.example.repository.PhotoTagUserRepository;
import com.example.repository.UserRepository;
import com.example.service.PhotoStorageService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/engineTag")

public class EngineAndHandTagUserController {
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	PhotoTagUserRepository phototaguserRepository;
	@Autowired
	PhotoStorageService photoStorageService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	GetResult getResult;

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
	// 沒有api所以我把pathvariable這些刪掉囉
	public void engineTag(String personId, String imageSourcePath, String facepath) throws IOException  {
		String photoid = findPhotoIdByPhotoPath(imageSourcePath);
		User user = new User(personId);
		String path = "C:/engine/"+facepath;
		
		
		//將File convert to MultipartFile
        File face = new File(path);
        FileInputStream readfile = new FileInputStream(face);
        MultipartFile multi = new MockMultipartFile(path,readfile);
        String faceUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/engineTag/downloadFace/")
				.path(facepath).toUriString();
        
        System.out.println("--------");
        System.out.println("multidata: " + multi.getBytes());
        System.out.println("uri: " + faceUri);
        System.out.println("user: " + user);
        System.out.println("facepath: " + facepath);
        System.out.println("photoid: " + photoid);
        System.out.println("--------");
       
		 photoRepository.findById(photoid).map(photodata->{
			 
			 System.out.println("photodata: " + photodata.getId());
			
			Photo photo = new Photo(photoid);
			PhotoTagUser ptu = null;
			Long diaryId = photodata.getDiary().getId();
			
			try {
				 ptu = new PhotoTagUser(photo, user, diaryId, path, multi.getBytes(), faceUri);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return phototaguserRepository.save(ptu);
		 
		}).orElseThrow(() -> new BadRequestException("PhotoId" + photoid + "not found"));
		 //不知道為什麼這個部分拿掉，資料就可以成功新增，只要增加這個，就會出現錯誤，但卻不是真的找不到photoId
		
		 
	}

	// 手動標記 一張照片標記很多個人
	public Photo handTag(@PathVariable(value = "photoId") String photoId, @RequestParam("username") String personId,
			String facepath) {
		String username = findUsernameByPersonId(personId);
		return photoRepository.findById(photoId).map(photo -> {
			User user = new User(username);
			Long diaryId = photo.getDiary().getId();
			// photo.getUser().add(user);
			//photo.addUser(user, diaryId, facepath);
			return photoRepository.save(photo);
		}).orElseThrow(() -> new BadRequestException("PhotoId " + photoId + " not found"));

	}

	@PostMapping("/{photoId}")
	public List<String> handTags(@PathVariable(value = "photoId") String photoId,
			@RequestParam("username") String[] username, String facepath) {
		if (username != null && username.length > 0) {
			for (int i = 0; i < username.length; i++) {
				String user = username[i];
				//handTag(photoId, user, facepath);
			}
		}

		return Arrays.asList(username);
	}

}
