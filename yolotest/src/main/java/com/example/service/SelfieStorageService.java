package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Selfie;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.exception.MySelfieNotFoundException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.SelfieRepository;
import com.example.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

import java.util.function.Function;


@Service
public class SelfieStorageService {
	@Autowired
	private SelfieRepository selfieRepository;
	@Autowired
	private UserRepository userRepository;

	public Selfie storeSelfie(MultipartFile selfie, String username) {
		// Normalize file name
		String selfieName = StringUtils.cleanPath(selfie.getOriginalFilename());
		

		try {
			// Check if the file's name contains invalid characters
			if (selfieName.contains("..")) {
				throw new BadRequestException("Sorry! Filename contains invalid path sequence " + selfieName);
			}

			Selfie selfies = new Selfie(selfieName, selfie.getContentType(), selfie.getBytes());
			userRepository.findByUsername(username).map(user -> {
				user.setSelfie(selfies);
				return user;
			}).orElseThrow(() -> new ResourceNotFoundException("Username = " + username + "not found", null, null));
			
			
			
			return selfieRepository.save(selfies);
			
			
		} catch (IOException ex) {
			throw new BadRequestException("Could not store file " + selfieName + ". Please try again!", ex);
		}
	}

	public Selfie getSelfie(String selfieId) {
		return selfieRepository.findById(selfieId)
				.orElseThrow(() -> new MySelfieNotFoundException("File not found with id " + selfieId));
	}
	


}
