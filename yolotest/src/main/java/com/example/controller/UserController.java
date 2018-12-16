package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Selfie;
import com.example.entity.User;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.DiaryResponse;
import com.example.payload.PagedResponse;
import com.example.payload.UserIdentityAvailability;
import com.example.payload.UserProfile;
import com.example.payload.UserSummary;
import com.example.repository.SelfieRepository;
import com.example.repository.UserRepository;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import com.example.service.DiaryService;
import com.example.util.AppConstants;

/*
Get the currently logged in user.
Check if a username is available for registration.
Check if an email is available for registration.
Get the public profile of a user.
Get a paginated list of diaries created by a given user.
*/
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DiaryService diaryService;
	@Autowired
	SelfieRepository selfieRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	// 查詢全部的users
	@GetMapping
	public List<User> getAllUsers() {
		return userRepository.findAll();

	}

	@GetMapping("/me") // "username": "tiday0805"
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getUsername());
		return userSummary;
	}

	@GetMapping("/usernameEmail")
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUserAndEmail(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getUsername(), currentUser.getEmail());
		return userSummary;
	}

	@GetMapping("/checkUsernameAvailability") // 確認有無此帳號
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/checkEmailAvailability") // 確認有無此email
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/{username}") // "username": "tiday0805","joinedAt": "2018-08-05T03:11:15Z"
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		System.out.println(user);// com.example.entity.User@19a3673a
		UserProfile userProfile = new UserProfile(user.getUsername(), user.getEmail(), user.getCreatedAt());

		return userProfile;
	}

	// 取得某個使用者新增過的日記
	@GetMapping("/{username}/diaries")
	public PagedResponse<DiaryResponse> getDiariesCreatedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return diaryService.getDiariesCreatedBy(username, currentUser, page, size);
	}

	// 確認頭貼是否存在 存在"available": true 不存在"available": false
	@GetMapping("/mySelfie")
	public UserIdentityAvailability selfieCheck(@CurrentUser UserPrincipal currentUser) {
		String me = currentUser.getUsername();
		Boolean isAvailable;
		Optional<User> user = userRepository.findByUsername(me);
		System.out.println("me : " + me);
		if (user.get().getSelfie() != null) {
			System.out.println("selfie exist");
			isAvailable = false;
		} else {
			System.out.println("selfie isn't exist");
			isAvailable = true;
		}
		return new UserIdentityAvailability(isAvailable);
	}

}
