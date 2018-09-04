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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.engine.controller.EngineFunc;
import com.example.engine.controller.GetResult;
import com.example.engine.entity.Face;
import com.example.engine.util.Textfile;
import com.example.entity.Photo;
import com.example.exception.BadRequestException;
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
	@Autowired
	GetResult result;


	/**
	 * 新增日記 -->辨識人臉 -->辨識出是好友-->通知(hasFound:1) -->辨識不出是好友，但是是好友-->訓練(hasFound:0)
	 * -->辨識錯誤（將好友a辨識成好友b)
	 **/
	public static void blob(byte[] imageByte, String name) {
		BufferedImage image = null;
		try {
			// imageByte = DatatypeConverter.parseBase64Binary(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			bis.close();

			File outputfile = new File("C:\\Users\\Administrator\\Desktop\\Engine0818\\photo\\" + name);
			// --> C:\engine\photo\ -->windows's path
			// --> /Users/ines/Desktop/engine/photo/ -->ines's mac path

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
		String photoId = photo.getId();
		System.out.println(photoId);
		photoRepository.findById(photoId).map(set -> {
			set.setPhotoPath("C:\\Users\\Administrator\\Desktop\\Engine0818\\photo\\" + photo.getPhotoName()); // 在資料表photo中加入photoPath
			// --> C:\engine\photo\ --> windows's path
			// --> /Users/ines/Desktop/engine/photo/ --> ines's mac path
			return photoRepository.save(set);
		}).orElseThrow(() -> new BadRequestException("PhotoId" + photoId + "not found"));
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
				txt.getPhotopath("C:\\Users\\Administrator\\Desktop\\Engine0818\\photo\\", diaryId);
				// --> C:\\Users\\Administrator\\Desktop\\photo\\ --> rrou's path
				// --> C:\engine\photo\ --> laboratory's path
				// --> /Users/ines/Desktop/engine/photo --> ines's mac path
				engine.retrieveEngine();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			    result.getResult();
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

//刪除照片
	@DeleteMapping("/{diaryId}/{photoId}")
	public ResponseEntity<?> deletePhoto(@PathVariable(value = "diaryId") Long diaryId,
			@PathVariable(value = "photoId") String photoId) {
		if (!diaryRepository.existsById(diaryId)) {
			throw new BadRequestException("DiaryId " + diaryId + " not found");
		}
		return photoRepository.findById(photoId).map(photo -> {
			photoRepository.delete(photo);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new BadRequestException("PhotoId" + photoId + "not found"));
	}

}
