package com.example.util;

import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.entity.Album;
import com.example.entity.AlbumUser;
import com.example.entity.Diary;
import com.example.entity.Photo;
import com.example.entity.User;
import com.example.exception.ResourceNotFoundException;
import com.example.payload.AlbumResponse;
import com.example.payload.DiaryResponse;
import com.example.payload.PhotoResponse;
import com.example.payload.UserSummary;
import com.example.repository.AlbumRepository;
import com.example.repository.DiaryRepository;

@Service
//mapping the Diary entity to a DiaryResponse payload
//AES日記加密解密https://blog.csdn.net/hbcui1984/article/details/5201247
public class ModelMapper {

	public static DiaryResponse mapDiaryToDiaryResponse(Diary diary, User creator) {
		DiaryResponse diaryResponse = new DiaryResponse();
		diaryResponse.setId(diary.getId());
		diaryResponse.setCreatedBy(creator.getUsername());
		diaryResponse.setCreationDateTime(diary.getCreatedAt());
//		UserSummary creatorSummary = new UserSummary(creator.getUsername());
//		diaryResponse.setCreatedBy(creatorSummary);
		byte[] decryptFrom = parseHexStr2Byte(diary.getText());
		String password = "ahfcjuh645465645";
		byte[] decrypt = decrypt(decryptFrom, password);
		diaryResponse.setText(new String(decrypt));

		return diaryResponse;

	}

	public static AlbumResponse mapAlbumToAlbumResponse(Album album, User creator) {
		AlbumResponse albumResponse = new AlbumResponse();
		albumResponse.setId(album.getId());
//		UserSummary creatorSummary = new UserSummary(creator.getUsername());
//		diaryResponse.setCreatedBy(creatorSummary);
		albumResponse.setPhotoCover(album.getPhotoUri());
		albumResponse.setName(album.getName());
		java.util.List<Diary> diary = album.getDiary();
		albumResponse.setDiaries(diary);
		return albumResponse;

	}

	public static AlbumResponse mapAlbumToAlbumResponse(Album album) {
		AlbumResponse albumResponse = new AlbumResponse();
		albumResponse.setId(album.getId());
		albumResponse.setPhotoCover(album.getPhotoUri());
		albumResponse.setName(album.getName());
		java.util.List<Diary> diary = album.getDiary();
		albumResponse.setDiaries(diary);
		return albumResponse;

	}

	public static AlbumResponse mapAlbumUserToAlbumUserResponseByUsername(AlbumUser albumUser) {
		AlbumResponse albumResponse = new AlbumResponse();
		Instant time = albumUser.getAlbum().getCreatedAt();
		Instant nowTime = Instant.now();
		LocalDateTime nowTimes = LocalDateTime.ofInstant(nowTime, ZoneId.systemDefault());
		System.out.println("nowTimes : " + nowTimes);
		System.out.println("time : " + time);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
		String times = localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
//		Instant instant = ldt.toInstant(ZoneOffset.UTC);

		System.out.println("albumCreatedTime : " + localDateTime);
		albumResponse.setId(albumUser.getAlbum().getId());
		albumResponse.setPhotoCover(albumUser.getAlbum().getPhotoUri());
		albumResponse.setName(albumUser.getAlbum().getName());
		albumResponse.setCreatedAt(times);
		java.util.List<Diary> diary = albumUser.getAlbum().getDiary();
		System.out.println("diary.isEmpty() : " + diary.isEmpty());
		if (diary.isEmpty()) {
			albumResponse.setMessage("點進來泡泡新增日記吧！");
		}
		albumResponse.setDiaries(diary);

		return albumResponse;
//		System.out.printf("%s %d %d at %d:%d%n", ldt.getMonth(), ldt.getDayOfMonth(), ldt.getYear(), ldt.getHour(),
//				ldt.getMinute());
	}

	public static UserSummary mapAlbumUserToAlbumUserResponseByAlbumId(AlbumUser albumUser) {
		UserSummary userSummary = new UserSummary();
		userSummary.setUsername(albumUser.getUser().getUsername());
		userSummary.setEmail(albumUser.getUser().getEmail());
		userSummary.setSelfieData(albumUser.getUser().getSelfie().getSelfiedata());
		userSummary.setDiaryId(albumUser.getDiaryId());

		return userSummary;

	}

	public static DiaryResponse mapDiaryToDiaryResponse(Diary diary) {
		DiaryResponse diaryResponse = new DiaryResponse();
		diaryResponse.setId(diary.getId());
		diaryResponse.setCreatedBy(diary.getCreatedBy());
		diaryResponse.setCreationDateTime(diary.getCreatedAt());
//		UserSummary creatorSummary = new UserSummary(creator.getUsername());
//		diaryResponse.setCreatedBy(creatorSummary);
		byte[] decryptFrom = parseHexStr2Byte(diary.getText());
		String password = "ahfcjuh645465645";
		byte[] decrypt = decrypt(decryptFrom, password);
		diaryResponse.setText(new String(decrypt));

		return diaryResponse;

	}

	public static PhotoResponse mapPhotoToPhotoResponse(Photo photo) {
		PhotoResponse photoResponse = new PhotoResponse();
		photoResponse.setId(photo.getId());
		photoResponse.setPhotodata(photo.getPhotodata());
		return photoResponse;

	}

	public static byte[] decrypt(byte[] content, String password) {
		KeyGenerator kgen = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 解密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

//2-->16
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

//16-->2
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

}
