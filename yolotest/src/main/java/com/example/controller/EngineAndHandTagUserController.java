package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.engine.controller.GetResult;
import com.example.entity.Photo;
import com.example.entity.PhotoTagUser;
import com.example.entity.PhotoTagUserId;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.PhotoTagUserResponse;
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
	PhotoTagUserRepository photoTagUserRepository;
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

			return photoTagUserRepository.save(ptu);

		}).orElseThrow(() -> new BadRequestException("PhotoId" + photoid + "not found"));

	}

	// 修改photoTagUser標記人名
//	@PutMapping("/{photoId}/{username}")
//	public PhotoTagUser updatePhotoTagUser(@PathVariable(value = "photoId") String photoId,
//			@PathVariable(value = "username") String username, @Valid @RequestBody PhotoTagUserResponse photoTagUserRequest) {
//		Photo photo = new Photo(photoId);
//		User user = new User(username);
//		PhotoTagUserId photoTagUserId = new PhotoTagUserId(photo, user);
//		System.out.println("photoTagUserId "+photoTagUserId);
//		if (!photoTagUserRepository.existsById(photoTagUserId)) {
//			throw new BadRequestException("PhotoTagUserId " + photoTagUserId.getPhoto().getId() + "&"
//					+ photoTagUserId.getUser().getUsername() + " not found");
//		}
//		return photoTagUserRepository.findById(photoTagUserId).map(tag -> {
//			User users = new User(photoTagUserRequest.getUsername());
//			tag.setUser(users);
//			return photoTagUserRepository.save(tag);
//		}).orElseThrow(() -> new ResourceNotFoundException("Username not found", null, photoTagUserRequest));
//	}

	// 刪除photoTagUser那張人臉圖

	public void handTag(String photoId, String personId, String facepath) throws IOException {
		String username = findUsernameByPersonId(personId);
		User user = new User(username);
		String path = "C:/engine/" + facepath;
		// C:/engine/
		// /Users/ines/Desktop/photo --> ines mac's path
		File face = new File(path);
		FileInputStream readfile = new FileInputStream(face);
		MultipartFile multi = new MockMultipartFile(path, readfile);
		String random = getRandomString(10);
		String faceUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/engineTag/downloadFace/")
				.path(random).toUriString();
		photoRepository.findById(photoId).map(photo -> {
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
			return photoTagUserRepository.save(ptu);
		}).orElseThrow(() -> new BadRequestException("PhotoId " + photoId + " not found"));

	}

	// 手動標記-->將這張人臉圖拿去訓練 需要參數（使用者名稱、人臉圖路徑facePath）
	@PostMapping("/{photoId}")
	public List<String> handTags(@PathVariable(value = "photoId") String photoId,
			@RequestParam("username") String[] username, @RequestParam("facepath") String facepath) throws IOException {
		if (username != null && username.length > 0) {
			for (int i = 0; i < username.length; i++) {
				String user = username[i];
				handTag(photoId, user, facepath);
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

	// 修正人臉圖使用者名稱
	@PutMapping("/updateface/{username}")
	public PhotoTagUser updatefaceusername(@PathVariable String username, String new_username) {
		User new_user = new User(new_username);
		// 要用findby其他，等要怎麼找人臉圖確定後再寫
		return photoTagUserRepository.findByFaceRandom(username).map(faceuser -> {
			faceuser.setUser(new_user);
			return photoTagUserRepository.save(faceuser);
		}).orElseThrow(() -> new ResourceNotFoundException("face username=" + username + "Not Found", null, null));

	}

}
