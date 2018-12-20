package com.example.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Album;
import com.example.entity.AlbumUser;
import com.example.entity.AlbumUserId;
import com.example.entity.Diary;
import com.example.entity.Photo;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.AlbumResponse;
import com.example.payload.UserSummary;
import com.example.repository.AlbumRepository;
import com.example.repository.AlbumUserRepository;
import com.example.repository.DiaryRepository;
import com.example.repository.PhotoRepository;
import com.example.repository.UserRepository;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import com.example.service.AlbumService;
import com.example.util.AppConstants;
import com.example.util.ModelMapper;

@RestController
@RequestMapping("/api/album")

public class AlbumController {

	@Autowired
	AlbumRepository albumRepository;
	@Autowired
	private AlbumService albumService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	DiaryRepository diaryRepository;
	@Autowired
	AlbumUserRepository albumUserRepository;
	@Autowired
	PhotoRepository photoRepository;

	// 取得所有相簿
	@GetMapping
	public List<AlbumResponse> getAllAlbums(Pageable pageable) {
		Page<Album> albums = albumRepository.findAll(pageable);
		List<AlbumResponse> albumResponses = albums.map(album -> {
			return ModelMapper.mapAlbumToAlbumResponse(album);
		}).getContent();
		return albumResponses;
	}

	// 取得某個使用者新增過的相簿
	@GetMapping("/user")
	public Page<Album> getAllAlbumsByUserId(@RequestParam(value = "username") String createdBy, Pageable pageable) {
		return albumRepository.findByCreatedBy(createdBy, pageable);
	}

	// 取得自己新增過的相簿
	@GetMapping("/albums")
	public List<AlbumResponse> getDiariesCreatedBy(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return albumService.getAlbumsCreatedByMe(currentUser, page, size);
//		return albumService.getAlbumsCreatedByMe(currentUser);
	}

	// 找相同username出現的albumId（自己新增＆被標記的全部相簿）
	@GetMapping("/allAlbums")
	public List<AlbumResponse> getAllAlbumsOfMe(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

		System.out.println("< User is >" + currentUser.getUsername());

		return albumService.getAlbumsAboutMe(currentUser, page, size);
	}

	// 找相同albumId出現的username（同個相簿中包含的使用者-->顯示名稱、頭貼、信箱、日記ID）
	@GetMapping("/allUsers/{albumId}")
	public List<UserSummary> getAllUsersOfAlbum(@PathVariable String albumId,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return albumRepository.findById(albumId).map(album -> {
			return albumService.getUsersAboutAlbum(albumId, page, size);
		}).orElseThrow(() -> new BadRequestException("AlbumId " + albumId + " not found"));
	}

	// 取得某個相簿的相簿名稱
	@GetMapping("/{albumId}")
	public AlbumResponse getAlbumName(@PathVariable String albumId) {
		return albumRepository.findById(albumId).map(album -> {
			AlbumResponse albumResponse = new AlbumResponse(album.getName());
			return albumResponse;
		}).orElseThrow(() -> new BadRequestException("AlbumId " + albumId + " not found"));
	}

	// 新增相簿後加進AalbumUser資料表
	public void insertToAlbumUser(String albumId) {
		albumRepository.findById(albumId).map(albums -> {
			Optional<User> userr = userRepository.findById(albums.getCreatedBy());
			User user = userr.get();
			AlbumUser albumUser = null;
			albumUser = new AlbumUser(albums, user);
			return albumUserRepository.save(albumUser);
		}).orElseThrow(() -> new BadRequestException("AlbumId " + albumId + " not found"));

	}

	// 新增相簿
	@PostMapping
	public AlbumResponse createAlbum(@Valid @RequestBody Album album) {
		System.out.println(album.getName());
		albumRepository.save(album);
		System.out.println(album.getId());
		System.out.println(album.getCreatedBy());

		insertToAlbumUser(album.getId());
		AlbumResponse albumResponse = new AlbumResponse();
		albumResponse.setId(album.getId());
		albumResponse.setName(album.getName());
		albumResponse.setPhotoCover(album.getPhotoUri());
		return albumResponse;
	}

	// 搜尋相簿名稱找相簿
	@PostMapping("/findAlbum")
	public List<AlbumResponse> findAlbumByName(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "albumName") String albumName,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

		List<Album> albumss = albumRepository.findByNameIn(albumName);
		for (int i = 0; i < albumss.size(); i++) {
			albumss.get(i);
			System.out.println("albumss " + albumss.get(i).getName());
		}
		List<AlbumResponse> albumResponse = albumService.getAlbumsAboutMe(currentUser, page, size);
		List<AlbumResponse> getSearchResult = new ArrayList<AlbumResponse>();
		for (int i = 0; i < albumResponse.size(); i++) {
			albumResponse.get(i);
			boolean ornot = albumResponse.get(i).getName().contains(albumName);
			System.out.println("orNot !! " + ornot);
			if (albumResponse.get(i).getName().contains(albumName)) {
				System.out.println("albumResponse here ture find");
				getSearchResult.add(albumResponse.get(i));
				System.out.println("add in getSearchResult list");

			} else {
				System.out.println("albumResponse here no find");
			}
			System.out.println("albumResponse " + albumResponse.get(i).getName());

		}
		return getSearchResult;
	}

	// 依時間分類（一年內）
	@GetMapping("/findAlbumByYear")
	public List<AlbumResponse> findAlbumByYear(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return albumService.getAlbumsAboutMeByYear(currentUser, page, size);
	}

	// 依時間分類（一月內）
	@GetMapping("/findAlbumByMonth")
	public List<AlbumResponse> findAlbumByMonth(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return albumService.getAlbumsAboutMeByMonth(currentUser, page, size);
	}

	// 依時間分類（一週內）
	@GetMapping("/findAlbumByWeek")
	public List<AlbumResponse> findAlbumByWeek(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return albumService.getAlbumsAboutMeByWeek(currentUser, page, size);
	}

	// 修改相簿
	@PutMapping("/{albumId}")
	public Album updateAlbum(@PathVariable String albumId, @Valid @RequestBody Album albumRequest) {
		return albumRepository.findById(albumId).map(album -> {
			album.setName(albumRequest.getName());
			return albumRepository.save(album);
		}).orElseThrow(() -> new ResourceNotFoundException("AlbumId " + albumId + " not found", null, albumRequest));
	}

	// 刪除相簿
	@DeleteMapping("/{albumId}")
	public ResponseEntity<?> deleteAlbum(@PathVariable String albumId) {
		return albumRepository.findById(albumId).map(album -> {
			albumRepository.delete(album);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("AlbumId " + albumId + " not found", null, albumId));
	}

	// 刪除albumUser
	@DeleteMapping("/deleteAlbumUser/{albumId}")
	public void deleteAlbumUser(@PathVariable String albumId, @CurrentUser UserPrincipal currentUser,
			Pageable pageable) {
		String username = currentUser.getUsername();
		Optional<Diary> diary = diaryRepository.findByAlbumIdAndCreatedBy(albumId, username);
		String diaryId = diary.get().getId();

		Page<Photo> photo = photoRepository.findByDiaryId(diaryId, pageable);
		for (int i = 0; i < photo.getContent().size(); i++) {
			Photo photos = photo.getContent().get(i);
			photoRepository.delete(photos);
		}

		Page<Diary> diaryss = diaryRepository.findByAlbumId(albumId, pageable);

		System.out.println("delete in !");
		for (int j = 0; j < diaryss.getContent().size(); j++) {
			System.out.println("username " + username);
			String createdBy = diaryss.getContent().get(j).getCreatedBy();
			System.out.println("diaryss.getContent().get(j).getCreatedBy() " + createdBy);

			if (diaryss.getContent().get(j).getCreatedBy().equals(username)) {
				Optional<Diary> diaryyy = diaryRepository.findById(diaryss.getContent().get(j).getId());
				Diary diaryy = diaryyy.get();
				diaryRepository.delete(diaryy);
				System.out.println("delete Success !");

			} else {
				System.out.println("No Delete~");
			}

		}
		Optional<Album> album = albumRepository.findById(albumId);
		Optional<User> user = userRepository.findById(username);
		AlbumUserId albumUserId = new AlbumUserId(album.get(), user.get());
		Optional<AlbumUser> albumUser = albumUserRepository.findById(albumUserId);
		albumUserRepository.delete(albumUser.get());

	}
}
