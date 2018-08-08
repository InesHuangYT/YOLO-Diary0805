package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.DiaryResponse;
import com.example.payload.PagedResponse;
import com.example.payload.UserIdentityAvailability;
import com.example.payload.UserProfile;
import com.example.payload.UserSummary;
import com.example.repository.DiaryRepository;
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

	@GetMapping("/checkUsernameAvailability") // 確認帳號有無重複
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/checkEmailAvailability") // 確認email有無重複
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/{username}") // "username": "tiday0805","joinedAt": "2018-08-05T03:11:15Z"
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		UserProfile userProfile = new UserProfile(user.getUsername(), user.getCreatedAt());

		return userProfile;
	}

	@GetMapping("/{username}/diaries")
	public PagedResponse<DiaryResponse> getDiariesCreatedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return diaryService.getDiariesCreatedBy(username, currentUser, page, size);
	}

}
