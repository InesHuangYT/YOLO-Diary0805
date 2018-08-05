package com.example.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.entity.Role;
import com.example.entity.RoleName;
import com.example.entity.User;
import com.example.exception.AppException;
import com.example.payload.ApiResponse;
import com.example.payload.JwtAuthenticationResponse;
import com.example.payload.LoginRequest;
import com.example.payload.SignUpRequest;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenProvider;

//https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/
@RestController
@RequestMapping("/api/auth")

public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	// 查詢全部的users
	@GetMapping("/user")
	public List<User> getAllUsers() {
		return userRepository.findAll();

	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/private")
	public String privateArea() {
		System.out.println("privateArea");
		return "bojour";

	}

	// 登入
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		System.out.println(authentication.getAuthorities());
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	// 註冊
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}

		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("User Role not set."));
		System.out.println(userRole);

		user.setRoles(Collections.singleton(userRole));
		System.out.println(Collections.singleton(userRole));// [com.example.entity.Role@3eb19954]

		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();
		System.out.println(location);//http://localhost:8080/api/users/testin221111

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}

	// 更改(failed)
	@RequestMapping(value = "user/{username}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(@RequestBody User user) {
		userRepository.save(user);

	}

	// 刪除
	@RequestMapping(value = "user/{username}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String username) {
		userRepository.deleteById(username);

	}

}
