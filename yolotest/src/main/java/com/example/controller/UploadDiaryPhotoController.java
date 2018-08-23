package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.repository.DiaryRepository;
import com.example.repository.PhotoRepository;
import com.example.service.PhotoStorageService;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/photo")
public class UploadDiaryPhotoController {
	@Autowired
	PhotoStorageService photoStorageService;
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	DiaryRepository diaryRepository;

	public static void blob(byte[] imageByte) {
		BufferedImage image = null;
		try {
		//	imageByte = DatatypeConverter.parseBase64Binary(imageString);
			ByteArrayInputStream bis = new  ByteArrayInputStream(imageByte);
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			bis.close();
			File outputfile = new File("/Users/ines/Desktop/photo/");
			ImageIO.write(image,"jpg", outputfile);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public UploadPhotoResponse uploadPhoto(@RequestParam("file") MultipartFile file, Long diaryId) {
		Photo photo = photoStorageService.storePhoto(file, diaryId);
		String photoDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/photo/downloadPhoto/")
				.path(photo.getId()).toUriString();
		photo.setPhotoUri(photoDownloadURI);
		photoRepository.save(photo);
		blob(photo.getPhotodata());
		return new UploadPhotoResponse(photo.getPhotoName(), file.getContentType(), photoDownloadURI, file.getSize());

	}

//上傳照片
	@PostMapping("/{diaryId}")
	public List<UploadPhotoResponse> uploadPhotos(@RequestParam("file") MultipartFile[] file,
			@PathVariable(value = "diaryId") Long diaryId) {
		return Arrays.asList(file).stream().map(files -> uploadPhoto(files, diaryId)).collect(Collectors.toList());
	}

//下載照片
	@GetMapping("/downloadPhoto/{photoId}")
	public ResponseEntity<Resource> downloadPhoto(@PathVariable String photoId) {
		Photo photo = photoStorageService.getPhoto(photoId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(photo.getPhotoType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; photoname = \"" + photo.getPhotoName() + "\"")
				.body(new ByteArrayResource(photo.getPhotodata()));
	}
//讀取某日記中所上傳的照片
//	@GetMapping
//	public List<Photo> getAllPhotos(){
//		return photoRepository.findAll();
//	}

}
