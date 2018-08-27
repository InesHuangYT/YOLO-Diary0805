package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.engine.controller.EngineFunc;
import com.example.engine.entity.RetrieveFace;
import com.example.engine.entity.TrainFace;
import com.example.engine.util.CmdUtil;
import com.example.engine.util.Textfile;
import com.example.engine.util.TxtUtil;
import com.example.entity.Selfie;
import com.example.payload.UploadSelfieResponse;
import com.example.repository.SelfieRepository;
import com.example.repository.UserRepository;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import com.example.service.SelfieStorageService;
import com.mysql.fabric.xmlrpc.base.Array;

@RestController
@RequestMapping("/api/selfie")
//MultipartException: Current request is not a multipart request
//https://stackoverflow.com/questions/42013087/multipartexception-current-request-is-not-a-multipart-request
public class UploadSelfieController {

	private static final Logger logger = LoggerFactory.getLogger(UploadSelfieController.class);

	@Autowired
	SelfieStorageService selfieStorageService;

	@Autowired
	SelfieRepository selfieRepository;

	@Autowired
	Textfile txt;

	@Autowired
	EngineFunc engine;

//將檔案blob轉成絕對路徑
	public static void blob(byte[] imageByte, String name) {

		BufferedImage image = null;
		try {
			// imageByte = DatatypeConverter.parseBase64Binary(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			bis.close();

			File outputfile = new File("C:\\eGroupAI_FaceRecognitionEngine_V3.0\\selfie\\" + name);
			// /Users/ines/Desktop/photo --> ines mac's path
			// C:\\Users\\Administrator\\Desktop\\photo\\ --> rrou's path
//			trainEngine("C:\\Users\\Administrator\\Desktop\\photo\\",current, i);
//			trainEngine("C:\\Users\\Administrator\\Desktop\\photo\\+i+.jpg",current);
			ImageIO.write(image, "jpg", outputfile);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

//上傳頭貼
	private UploadSelfieResponse uploadSelfie(@RequestParam("file") MultipartFile file, @CurrentUser String current) {// @PathVariable(value
																														// =
																														// "username")

		Selfie selfie = selfieStorageService.storeSelfie(file, current);

		String selfieDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/selfie/downloadSelfie/").path(selfie.getId()).toUriString();
		System.out.println(selfieDownloadURI);
		selfie.setSelfieUri(selfieDownloadURI);
		selfieRepository.save(selfie);
		blob(selfie.getSelfiedata(), selfie.getSelfieName());
		return new UploadSelfieResponse(selfie.getSelfieName(), file.getContentType(), selfieDownloadURI,
				file.getSize());

	}

//上傳頭貼
	@PostMapping("/uploadmany")
	public List<UploadSelfieResponse> uploadSelfies(@RequestParam("file") MultipartFile[] file,
			@CurrentUser UserPrincipal currentUser) {

		String username = currentUser.getUsername();
		if (file != null && file.length > 0) {

			for (int i = 0; i < file.length; i++) {
				System.out.println(i + ":" + "第" + i + "張照片");
				System.out.println("共" + (i + 1) + "張照片");

				MultipartFile savefile = file[i];
				uploadSelfie(savefile, username);

			}

			try {
				txt.getSelfiepath("C:\\eGroupAI_FaceRecognitionEngine_V3.0\\selfie\\", currentUser.getUsername());
				// C:\\Users\\Administrator\\Desktop\\photo\\ --> rrou's path
				
				engine.trainEngine();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	@GetMapping("/downloadSelfie/{selfieId}")
	public ResponseEntity<Resource> downloadSelfie(@PathVariable String selfieId) {

		Selfie selfie = selfieStorageService.getSelfie(selfieId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(selfie.getSelfieType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; selfiename = \"" + selfie.getSelfieName() + "\"")
				.body(new ByteArrayResource(selfie.getSelfiedata()));
	}

}