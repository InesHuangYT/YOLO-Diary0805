package com.example.controller;

import java.io.IOException;
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
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.AlbumResponse;
import com.example.payload.UserSummary;
import com.example.repository.AlbumRepository;
import com.example.repository.AlbumUserRepository;
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
	AlbumUserRepository albumUserRepository;

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
		return albumService.getAlbumsAboutMe(currentUser, page, size);
	}
	// 找相同albumId出現的username（相簿中包含的使用者名稱）
	@GetMapping("/allUsers/{albumId}")
	public List<UserSummary> getAllUsersOfAlbum(@PathVariable String albumId,
	@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
	@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
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

	//新增相簿後加進AalbumUser資料表
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
		// Add user references in the album
//		Optional<User> user = userRepository.findByUsername(album.getCreatedBy());
//		User users = user.get();
//		album.getUsers().add(users);
//		users.getAlbums().add(album);
//		System.out.println("add this");
//		albumRepository.save(album);
//		System.out.println("finish this");
		insertToAlbumUser(album.getId());
		AlbumResponse albumResponse = new AlbumResponse();
		albumResponse.setId(album.getId());
		albumResponse.setName(album.getName());
		albumResponse.setPhotoCover(album.getPhotoUri());
		return albumResponse;
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
}
