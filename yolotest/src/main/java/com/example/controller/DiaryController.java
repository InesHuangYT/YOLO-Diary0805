package com.example.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.entity.Album;
import com.example.entity.Diary;
import com.example.exception.BadRequestException;
import com.example.payload.ApiResponse;
import com.example.payload.DiaryRequest;
import com.example.payload.DiaryResponse;
import com.example.payload.PagedResponse;
import com.example.repository.AlbumRepository;
import com.example.repository.DiaryRepository;
import com.example.repository.UserRepository;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import com.example.service.DiaryService;
import com.example.util.AppConstants;

@RestController
@RequestMapping("/api/album/{albumId}")
//要在那一本相簿的哪一篇日記 做新增刪除修改

public class DiaryController {

	@Autowired
	private DiaryRepository diaryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private DiaryService diaryService;

	private static final Logger logger = LoggerFactory.getLogger(DiaryController.class);

	@GetMapping
	public PagedResponse<DiaryResponse> getDiaries(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return diaryService.getAllDiaries(currentUser, page, size);
	}

	@GetMapping("/diaries")
	public Page<Diary> getAllDiariesByAlbumId(@PathVariable(value = "albumId") Long albumId, Pageable pageable) {
		return diaryRepository.findByAlbumId(albumId, pageable);
	}

	// 新增日記
	@PostMapping("/diaries")
	public Diary createDiary(@PathVariable(value = "albumId") Long albumId, @Valid @RequestBody Diary diary) {
		return albumRepository.findById(albumId).map(album -> {
			diary.setAlbum(album);
			return diaryRepository.save(diary);
		}).orElseThrow(() -> new BadRequestException("AlbumId " + albumId + " not found"));
	}

	// 修改日記
	@PutMapping("/diaries/{diaryId}")
	public Diary updateDiary(@PathVariable(value = "albumId") Long albumId,
			@PathVariable(value = "diaryId") Long diaryId, @Valid @RequestBody Diary diaryRequest) {
		if (!albumRepository.existsById(albumId)) {
			throw new BadRequestException("AlbumId " + albumId + " not found");
		}

		return diaryRepository.findById(diaryId).map(diary -> {
			diary.setText(diaryRequest.getText());
			return diaryRepository.save(diary);
		}).orElseThrow(() -> new BadRequestException("DiaryId " + diaryId + "not found"));
	}

	// 刪除日記
	@DeleteMapping("/diaries/{diaryId}")
	public ResponseEntity<?> deleteDiary(@PathVariable(value = "albumId") Long albumId,
			@PathVariable(value = "diaryId") Long diaryId) {
		if (!albumRepository.existsById(albumId)) {
			throw new BadRequestException("AlbumId " + albumId + " not found");
		}

		return diaryRepository.findById(diaryId).map(diary -> {
			diaryRepository.delete(diary);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new BadRequestException("DiaryId " + diaryId + " not found"));
	}

//	@PostMapping //新增日記
//	@PreAuthorize("hasRole('USER')")
//	public ResponseEntity<?> createDiary(@Valid @RequestBody DiaryRequest diaryRequest) {
//
//		Diary diary = diaryService.createDiary(diaryRequest);
//
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{diaryId}").buildAndExpand(diary.getId())
//				.toUri();
//
//		return ResponseEntity.created(location).body(new ApiResponse(true, "Diary Created Successfully"));
//	}
//	
//	  @GetMapping("/{diaryId}")
//	    public DiaryResponse getDiaryById(@CurrentUser UserPrincipal currentUser,
//	                                    @PathVariable Long diaryId) {
//	        return diaryService.getDiaryById(diaryId, currentUser);
//	    }

}
