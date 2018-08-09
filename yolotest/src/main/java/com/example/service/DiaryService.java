package com.example.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.entity.Diary;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.DiaryResponse;
import com.example.payload.PagedResponse;
import com.example.repository.DiaryRepository;
import com.example.repository.UserRepository;
import com.example.security.UserPrincipal;
import com.example.util.AppConstants;
import com.example.util.ModelMapper;

@Service
public class DiaryService {
	@Autowired
	private DiaryRepository diaryRepository;
	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(DiaryService.class);

//UserController
	public PagedResponse<DiaryResponse> getDiariesCreatedBy(String username, UserPrincipal currentUser, int page,
			int size) {
		validatePageNumberAndSize(page, size);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		// Retrieve all diaries created by the given username

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Diary> diaries = diaryRepository.findByCreatedBy(user.getUsername(), pageable);
		if (diaries.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), diaries.getNumber(), diaries.getSize(),
					diaries.getTotalElements(), diaries.getTotalPages(), diaries.isLast());
		}

		// Map Diaries to DiaryResponses containing diary creator details

		List<Long> diaryIds = diaries.map(Diary::getId).getContent();
		List<DiaryResponse> diaryResponses = diaries.map(diary -> {
			return ModelMapper.mapDiaryToDiaryResponse(diary, user);
		}).getContent();
		return new PagedResponse<>(diaryResponses, diaries.getNumber(), diaries.getSize(), diaries.getTotalElements(),
				diaries.getTotalPages(), diaries.isLast());

	}

	/* 以下為上方有使用到的方法，validatePageNumberAndSize、getDiaryCreatorMap */

	private void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

//Get Diary Creator details of the given list of diaries
	Map<String, User> getDiaryCreatorMap(List<Diary> diaries) { // use String not long
		List<String> creatorIds = diaries.stream().map(Diary::getCreatedBy).distinct().collect(Collectors.toList());
		List<User> creators = userRepository.findByUsername(creatorIds);
		Map<String, User> creatorMap = creators.stream()
				.collect(Collectors.toMap(User::getUsername, Function.identity()));
		return creatorMap;

	}



//DiaryController
//	public DiaryResponse getDiaryById(Long diaryId, UserPrincipal currentUser) {
//		Diary diary = diaryRepository.findById(diaryId)
//				.orElseThrow(() -> new ResourceNotFoundException("Diary", "id", diaryId));
//		
//        // Retrieve diary creator details
//		User creator = userRepository.findById(diary.getCreatedBy())
//				.orElseThrow(() -> new ResourceNotFoundException("User", "username", diary.getCreatedBy()));// use
//																											// username
//		return ModelMapper.mapDiaryToDiaryResponse(diary, creator);																							// not id
//
//	}

//	DiaryController
//	public PagedResponse<DiaryResponse> getAllDiaries(UserPrincipal currentUser, int page, int size) {
//		validatePageNumberAndSize(page, size);
//
//		// Retrieve Diaries
//		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
//		Page<Diary> diaries = diaryRepository.findAll(pageable);
//
//		if (diaries.getNumberOfElements() == 0) {
//			return new PagedResponse<>(Collections.emptyList(), diaries.getNumber(), diaries.getSize(),
//					diaries.getTotalElements(), diaries.getTotalPages(), diaries.isLast());
//		}
//
//		// Map Diaries to DiaryResponses containing diary creator details
//
//		List<Long> diaryIds = diaries.map(Diary::getId).getContent();
//
//		Map<String, User> creatorMap = getDiaryCreatorMap(diaries.getContent());
//
//		List<DiaryResponse> diaryResponses = diaries.map(diary -> {
//			return ModelMapper.mapDiaryToDiaryResponse(diary, creatorMap.get(diary.getCreatedBy()));
//		}).getContent();
//
//		return new PagedResponse<>(diaryResponses, diaries.getNumber(), diaries.getSize(), diaries.getTotalElements(),
//				diaries.getTotalPages(), diaries.isLast());
//
//	}

	/* 要使用上面getAllDiaries的方法 在controller要加以下的程式 */
//	@GetMapping
//	public PagedResponse<DiaryResponse> getAllDiaries(@CurrentUser UserPrincipal currentUser,
//			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
//			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
//		return diaryService.getAllDiaries(currentUser, page, size);
//	}

}
