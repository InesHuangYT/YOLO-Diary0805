package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.engine.controller.GetResult;
import com.example.entity.Photo;
import com.example.entity.PhotoTagUser;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.repository.PhotoRepository;
import com.example.repository.PhotoTagUserRepository;
import com.example.repository.UserRepository;
import com.example.service.PhotoStorageService;

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
	public void engineTag(String personId, String imageSourcePath, String facepath) throws IOException {
		String photoid = findPhotoIdByPhotoPath(imageSourcePath);
		User user = new User(personId);
		String path = "C:/engine/" + facepath;

		// 將File convert to MultipartFile
		File face = new File(path);
		FileInputStream readfile = new FileInputStream(face);
		MultipartFile multi = new MockMultipartFile(path, readfile);
		String random = getRandomString(10);
		String faceUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/engineTag/downloadFace/")
				.path(random).toUriString();
		System.out.println("--------");
		System.out.println("multidata: " + multi.getBytes());
		System.out.println("uri: " + faceUri);
		System.out.println("user: " + user);
		System.out.println("facepath: " + facepath);
		System.out.println("photoid: " + photoid);
		System.out.println("--------");

		photoRepository.findById(photoid).map(photodata -> {

			System.out.println("photodata: " + photodata.getId());

			Photo photo = new Photo(photoid);
			PhotoTagUser ptu = null;
			Long diaryId = photodata.getDiary().getId();

			try {
				ptu = new PhotoTagUser(photo, user, diaryId, path, multi.getBytes(), faceUri, random);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return phototaguserRepository.save(ptu);

		}).orElseThrow(() -> new BadRequestException("PhotoId" + photoid + "not found"));

	}

	// 手動標記 一張照片標記很多個人
	public Photo handTag(String photoId, @RequestParam("username") String personId, String facepath, String faceUri) throws IOException {
		String username = findUsernameByPersonId(personId);
		User user = new User(username);
		String path = "C:/engine/" + facepath;
		File face = new File(path);
		FileInputStream readfile = new FileInputStream(face);
		MultipartFile multi = new MockMultipartFile(path, readfile);
		String random = getRandomString(10);
		return photoRepository.findById(photoId).map(photo -> {
//			User user = new User(username);
//			Long diaryId = photo.getDiary().getId();
//			 photo.getUser().add(user);
//			 photo.addUser(user, diaryId, facepath);
			PhotoTagUser ptu = null;
			Long diaryId = photo.getDiary().getId();
			try {
				ptu = new PhotoTagUser(photo, user, diaryId, path, multi.getBytes(), faceUri, random);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return photoRepository.save(photo);
		}).orElseThrow(() -> new BadRequestException("PhotoId " + photoId + " not found"));

	}

	@PostMapping("/{photoId}")
	public List<String> handTags(@PathVariable(value = "photoId") String photoId,
			@RequestParam("username") String[] username, @RequestParam("facepath")String facepath,@RequestParam("faceUri")String faceUri) throws IOException {
		if (username != null && username.length > 0) {
			for (int i = 0; i < username.length; i++) {
				String user = username[i];
				handTag(photoId, user, facepath,faceUri);
			}
		}

		return Arrays.asList(username);
	}

	// 下載人臉圖
	@GetMapping("/downloadFace/{random}")
	public ResponseEntity<Resource> downloadPhoto(@PathVariable String random) throws IOException {
		PhotoTagUser photoTagUser = photoStorageService.getPhotoFace(random);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/jpeg"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; photoname = \"" + "Face" + "\"")
				.body(new ByteArrayResource(photoTagUser.getFace_data()));
	}

	// 取亂數字串
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

}
