package com.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.entity.Photo;
import com.example.payload.UploadPhotoResponse;
import com.example.repository.PhotoRepository;
import com.example.service.PhotoStorageService;

@RestController
@RequestMapping("/api/photo")
public class UploadDiaryPhotoController {
	@Autowired
	PhotoStorageService photoStorageService;
	@Autowired
	PhotoRepository photoRepository;

	public UploadPhotoResponse uploadPhoto(@RequestParam("file") MultipartFile file, Long diaryId) {
		Photo photo = photoStorageService.storePhoto(file, diaryId);
		String photoDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/photo/downloadPhoto/").path(photo.getId()).toUriString();
		photo.setPhotoUri(photoDownloadURI);
		photoRepository.save(photo);
		return new UploadPhotoResponse(photo.getPhotoName(), file.getContentType(), photoDownloadURI, file.getSize());

	}
//上傳照片
	@PostMapping("/{diaryId}")
	public List<UploadPhotoResponse> uploadPhotos(@RequestParam("file") MultipartFile[] file,
			@PathVariable(value = "diaryId") Long diaryId) {
		return Arrays.asList(file).stream().map(files -> uploadPhoto(files, diaryId)).collect(Collectors.toList());
	}
//讀取照片
	@GetMapping("/downloadPhoto/{photoId}")
	public ResponseEntity<Resource> downloadPhoto(@PathVariable String photoId) {
		Photo photo = photoStorageService.getPhoto(photoId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(photo.getPhotoType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; photoname = \"" + photo.getPhotoName() + "\"")
				.body(new ByteArrayResource(photo.getPhotodata()));
	}
//讀取某日記中所上傳的照片
	//@GetMapping("/downloadPhoto/{diaryId}")
	

}
