package com.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.entity.Selfie;
import com.example.payload.UploadSelfieResponse;
import com.example.repository.SelfieRepository;
import com.example.repository.UserRepository;
import com.example.service.SelfieStorageService;

@RestController
@RequestMapping("/api/selfie")

public class UploadSelfieController {
	private static final Logger logger = LoggerFactory.getLogger(UploadSelfieController.class);

	@Autowired
	SelfieStorageService selfieStorageService;
	@Autowired
	SelfieRepository selfieRepository;
	@Autowired
	UserRepository userRepository ;

	@PostMapping("/upload/{username}")
	public UploadSelfieResponse uploadSelfie(@RequestParam("file") MultipartFile file,@PathVariable(value = "username") String username) {
		Selfie selfie = selfieStorageService.storeSelfie(file,username);
		String selfieDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/selfie/downloadSelfie/")
				.path(selfie.getId()).toUriString();
		return new UploadSelfieResponse(selfie.getSelfieName(), file.getContentType(), selfieDownloadURI,
				file.getSize());

	}
	
	@PostMapping("/uploadmany/{username}")
	public List<UploadSelfieResponse> uploadSelfies(@RequestParam("file") MultipartFile[] file,@PathVariable(value = "username") String username){
		return Arrays.asList(file).stream().map(files -> uploadSelfie(files,username)).collect(Collectors.toList());		
	}
	
	@GetMapping("/downloadSelfie/{selfieId}")
	public ResponseEntity<Resource> downloadSelfie(@PathVariable String selfieId){
		Selfie selfie = selfieStorageService.getSelfie(selfieId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(selfie.getSelfieType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; selfiename = \"" + selfie.getSelfieName() + "\"")
				.body(new ByteArrayResource (selfie.getSelfiedata()));
	}

	

}