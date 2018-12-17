package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.engine.controller.EngineFunc;
import com.example.engine.controller.GetResult;
import com.example.engine.entity.Face;
import com.example.engine.util.Textfile;
import com.example.entity.Album;
import com.example.entity.Diary;
import com.example.entity.RecUser;
import com.example.entity.Notice;
import com.example.entity.Photo;
import com.example.exception.BadRequestException;
import com.example.payload.AlbumResponse;
import com.example.payload.NotFoundFaceResponse;
import com.example.payload.PagedResponse;
import com.example.payload.PhotoResponse;
import com.example.payload.SaveFaceResponse;
import com.example.payload.UploadPhotoResponse;
import com.example.repository.AlbumRepository;
import com.example.repository.DiaryRepository;
import com.example.repository.NoticeRepository;
import com.example.repository.PhotoRepository;
import com.example.security.CurrentUser;
import com.example.service.AlbumService;
import com.example.service.PhotoStorageService;
import com.example.util.ModelMapper;

@RestController
@RequestMapping("/api/photo")
public class UploadDiaryPhotoController {

	static String PhotoFILEPATH = "C:/engine/photo/";
	// --> C:/engine/photo/ -->windows's path
	// --> /Users/ines/Desktop/engine/photo/ -->ines's mac path
	// --> C:/Users/Administrator/Desktop/Engine0818/photo/ -->rou's path

	@Autowired
	PhotoStorageService photoStorageService;
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	DiaryRepository diaryRepository;
	@Autowired
	AlbumService albumService;
	@Autowired
	Textfile txt;
	@Autowired
	EngineFunc engine;
	@Autowired
	GetResult result;
	@Autowired
	EngineAndHandTagUserController engineAndHandTagUserController;
	@Autowired
	AlbumRepository albumRepository;
	@Autowired
	NoticeRepository noticerepository;

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

			File outputfile = new File(PhotoFILEPATH + name);

			ImageIO.write(image, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String uploadPhoto(MultipartFile file, String diaryId, Long batchid) {
		Photo photo = photoStorageService.storePhoto(file, diaryId);
		String catchuri = null;
		String photoDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/photo/downloadPhoto/")
				.path(photo.getId()).toUriString();
		photo.setPhotoUri(photoDownloadURI);
		photo.setBatchid(batchid);
		photoRepository.save(photo);
		blob(photo.getPhotodata(), photo.getPhotoName());

		String photoId = photo.getId();
		System.out.println(photoId);
		catchuri = photoRepository.findById(photoId).map(set -> {
			set.setPhotoPath(PhotoFILEPATH + photo.getPhotoName()); // 在資料表photo中加入photoPath

			Optional<Diary> diary = diaryRepository.findById(diaryId);
			Album album = diary.get().getAlbum();
			String uri;
			System.out.println("albumId : " + album.getId());
			System.out.println("Here photoUri : " + album.getPhotoUri());
			boolean f = album.getPhotoUri() == null;
			System.out.println("Here " + f);

			if (album.getPhotoUri() == null) {
				System.out.println("photoUri : " + album.getPhotoUri());
				Optional<Photo> photos = photoRepository.findByDiary(diary.get());
				album.setPhotoUri(photos.get().getPhotoUri());
				albumRepository.save(album);
			}

			System.out.println("SetphotoUri : " + album.getPhotoUri());
			uri = album.getPhotoUri();
			photoRepository.save(set);
			return uri;
		}).orElseThrow(() -> new BadRequestException("PhotoId" + photoId + "not found"));

		return catchuri;

	}

//上傳照片

	@RequestMapping(value = "/{diaryId}", headers = "content-type=multipart/*", method = RequestMethod.POST)

	public UploadPhotoResponse uploadPhotos(@CurrentUser Principal currentUer,
			@RequestParam(value = "file", required = true) MultipartFile[] file,
			@PathVariable(value = "diaryId") String diaryId) throws IOException {

		System.out.println("upload photo!!!!!!!!!!!!!!(" + file.length + ")");
		List<Face> faceList = new ArrayList<>();
//		List<UploadPhotoResponse> Lupr = new ArrayList<>();
		List<SaveFaceResponse> Lsfr = new ArrayList<>();
		List<NotFoundFaceResponse> Lnffr = new ArrayList<>();
		Random ran = new Random();
		long batchid = ran.nextInt(10000000);
		String catchCoverUri = null;

		if (file != null && file.length > 0) {
			for (int i = 0; i < file.length; i++) {
				System.out.println("上傳照片 ： " + file.length);
				System.out.println("第" + (i + 1) + "張");
				System.out.println("共" + (i + 1) + "張照片");
				MultipartFile savefile = file[i];
				catchCoverUri = uploadPhoto(savefile, diaryId, batchid);
				System.out.println("--------");
				System.out.println("Cover Uri:" + catchCoverUri);
				System.out.println("--------");
			}

			try {
				txt.getPhotopath(PhotoFILEPATH, diaryId);
				engine.retrieveEngine();
				faceList = result.getResult();

				// 利用hashmap知道整篇日記有在照片中出現過的人(一次)
				HashMap<String, RecUser> hashmap = new HashMap();

				for (int i = 0; i < faceList.size(); i++) {
					int hasFound = Integer.valueOf(faceList.get(i).getHasFound());
					// 辨識出的使用者不是本人就開始標記
					if (hasFound == 1 && !currentUer.getName().equals(faceList.get(i).getPersonId())) {
						// 同一個被標記的使用者只標記一次
						hashmap.putIfAbsent(faceList.get(i).getPersonId(), new RecUser(faceList.get(i).getPersonId(), faceList.get(i).getImageSourcePath(),
								faceList.get(i).getFrameFace().getFrameFacePath()));
								
						System.out.println("here is after getResult mathod : " + faceList.get(i).getPersonId());
						System.out.println("here is after getResult mathod : " + faceList.get(i).getImageSourcePath());


					} else if (hasFound == 0) {
						System.out.println(
								"|||||Face Not Found Here||||" + faceList.get(i).getFrameFace().getFrameFacePath());
						NotFoundFaceResponse notfoundFaceRes = engineAndHandTagUserController
								.FaceNotFound(faceList.get(i).getFrameFace().getFrameFacePath());
						Lnffr.add(notfoundFaceRes);

					}
					
					// 標記存入資料表
					
				}
				
				for (Object key : hashmap.keySet()) {

					System.out.println("---------------------");
					System.out.println(key + " : " + hashmap.get(key).getPersonId());
					System.out.println(key + " : " + hashmap.get(key).getImageSourcePath());
					System.out.println(key + " : " + hashmap.get(key).getFrameFacePath());
					System.out.println("---------------------");
					SaveFaceResponse sfr = engineAndHandTagUserController.engineTag(
							hashmap.get(key).getPersonId(), hashmap.get(key).getImageSourcePath(),
							hashmap.get(key).getFrameFacePath());
					Lsfr.add(sfr);
					hashmap.clear();

				}

				System.out.println("tag finish!");

				txt.deleteAllFile(PhotoFILEPATH);

			} catch (Exception e) {
				e.printStackTrace();
				txt.deleteAllFile(PhotoFILEPATH);
			}

		}

		return new UploadPhotoResponse(Lsfr, Lnffr, catchCoverUri);

	}

//取得同日記的所有相片
	@GetMapping("/downloadDiaryPhoto/{diaryId}")
	public List<PhotoResponse> getDiaryPhoto(@PathVariable(value = "diaryId") String diaryId, Pageable pageable) {
		Diary diary = new Diary(diaryId);
		Page<Photo> photos = photoRepository.findByDiary(diary, pageable);
		List<PhotoResponse> photoResponse = photos.map(photo -> {
			PhotoResponse photoResponses = new PhotoResponse();
			photoResponses.setId(photo.getId());
			photoResponses.setPhotodata(photo.getPhotodata());
			return photoResponses;
		}).getContent();

		System.out.println("text" + diary.getId());
		return photoResponse;
	}

// 取得同個相簿的所有照片 相簿id-日記id-照片id
	@GetMapping("/downloadAlbumPhoto/{albumId}")
	public List<PhotoResponse> getAlbumPhoto(@PathVariable(value = "albumId") String albumId, Pageable pageable) {

		Page<Photo> photo = photoRepository.findByAlbumId(albumId, pageable);
		List<PhotoResponse> photoResponse = photo.map(photos -> {
			return ModelMapper.mapPhotoToPhotoResponse(photos);
		}).getContent();
		return photoResponse;

	}

//下載照片
	@GetMapping("/downloadPhoto/{photoId}")
	public PhotoResponse getPhoto(@PathVariable String photoId) {
		return photoRepository.findById(photoId).map(photo -> {
			PhotoResponse photoResponse = new PhotoResponse(photoId, photo.getPhotodata());
			return photoResponse;
		}).orElseThrow(() -> new BadRequestException("PhotoId " + photoId + " not found"));
	}

//刪除照片（如果照片只有一張 那麼相簿封面要直接刪掉！）
	@DeleteMapping("/{diaryId}/{photoId}")
	public ResponseEntity<?> deletePhoto(@PathVariable(value = "diaryId") String diaryId,
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
