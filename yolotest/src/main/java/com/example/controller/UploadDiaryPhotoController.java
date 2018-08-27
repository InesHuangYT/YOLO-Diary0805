package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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

import com.example.engine.controller.EngineFunc;
import com.example.engine.util.Textfile;
import com.example.entity.Photo;
import com.example.payload.UploadPhotoResponse;
import com.example.repository.DiaryRepository;
import com.example.repository.PhotoRepository;
import com.example.service.PhotoStorageService;

@RestController
@RequestMapping("/api/photo")
public class UploadDiaryPhotoController {
	@Autowired
	PhotoStorageService photoStorageService;
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	DiaryRepository diaryRepository;
	@Autowired
	Textfile txt;
	@Autowired
	EngineFunc engine;
/** 新增日記
 * -->辨識人臉
 * -->辨識出是好友-->通知(hasFound:1)
 * -->辨識不出是好友，但是是好友-->訓練(hasFound:0)
 * -->辨識錯誤（將好友a辨識成好友b）
 * 
 * **/
	public static void blob(byte[] imageByte, String name) {
		BufferedImage image = null;
		try {
			// imageByte = DatatypeConverter.parseBase64Binary(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			bis.close();
			File outputfile = new File("C:\\eGroupAI_FaceRecognitionEngine_V3.0\\photo\\" + name );
			ImageIO.write(image, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UploadPhotoResponse uploadPhoto(@RequestParam("file") MultipartFile file, Long diaryId) {
		Photo photo = photoStorageService.storePhoto(file, diaryId);
		String photoDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/photo/downloadPhoto/")
				.path(photo.getId()).toUriString();
		photo.setPhotoUri(photoDownloadURI);
		photoRepository.save(photo);
		blob(photo.getPhotodata(), photo.getPhotoName());
		return new UploadPhotoResponse(photo.getPhotoName(), file.getContentType(), photoDownloadURI, file.getSize());

	}

//上傳照片
	@PostMapping("/{diaryId}")
	public List<UploadPhotoResponse> uploadPhotos(@RequestParam("file") MultipartFile[] file,
			@PathVariable(value = "diaryId") Long diaryId) {
		if (file != null && file.length > 0) {
			for (int i = 0; i < file.length; i++) {
				System.out.println("第" + (i + 1) + "張");
				System.out.println("共" + (i + 1) + "張照片");
				MultipartFile savefile = file[i];
				uploadPhoto(savefile, diaryId);
			}
			try {
				txt.getPhotopath("C:\\eGroupAI_FaceRecognitionEngine_V3.0\\photo\\", diaryId);
				// C:\\Users\\Administrator\\Desktop\\photo\\ --> rrou's path

				engine.retrieveEngine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

//下載照片
	@GetMapping("/downloadPhoto/{photoId}")
	public ResponseEntity<Resource> downloadPhoto(@PathVariable String photoId) {
		Photo photo = photoStorageService.getPhoto(photoId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(photo.getPhotoType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; photoname = \"" + photo.getPhotoName() + "\"")
				.body(new ByteArrayResource(photo.getPhotodata()));
	}
	
}
