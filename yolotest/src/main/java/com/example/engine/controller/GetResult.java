package com.example.engine.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.engine.entity.Face;
import com.example.engine.util.CopyUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** 
* @author 作者 Daniel
* @date 2018年8月16日 上午8:35:11 
* @version 
* @description:
*/
/**
 * 無法直接讀取回傳的 JSON?
 * 因辨識引擎持續辨識，程式需複製辨識中的 JSON 檔，再進行讀取。
*/
/**
 * Yoloapplication先執行 在執行GetResult 步驟:
 * I.上傳頭貼訓練，覆蓋list.txt和selfie中的圖片檔，我們要寫條件使用append!
 * II.上傳日記照片辨識，取快取結果，產出photolist.egroupList和photo中的圖片檔 -->辨識人臉
 * 1:辨識出是好友(hasFound:1) --> 標記進資料庫 --> 通知 2:-->辨識不出是好友(hasFound:0)，但是是好友-->
 * 手動標記進資料庫 --> 此face拿來訓練(append) 3:-->辨識錯誤（將好友a辨識成好友b）--> 手動標記正確使用者進資料庫 -->
 * 此face拿來訓練(append)
 **/
@Service
public class GetResult {

	static protected String ENGINEPATH = "C:\\engine";

	// --> C:\engine --> windows's path
	// --> /Users/ines/Desktop/engine --> ines'mac path
	// --> C:/Users/eGroup/Desktop/Engine -->rrou's path

	public List<Face> getResult() {

		List<Face> faceList = new ArrayList<>();

		// Get Real-time data 抓快取JSON檔
		String cacheJsonName = "output.cache.egroup"; // Get Real-time data

		long startTime = System.currentTimeMillis();
		faceList = getCacheResult(ENGINEPATH, cacheJsonName);
		System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,faceList="
				+ new Gson().toJson(faceList));
		if (faceList.size() > 0) {
			for (int i = 0; i < faceList.size(); i++) {
				System.out.println((i + 1) + "." + "Main hasFound : " + faceList.get(i).getHasFound());

			}
		}
		// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame =
		// 100 ms
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return faceList;
	}
	// Stop by yourself
	/*************************************************************************************************/

	// Get All Retrieve Data 抓整日JSON檔

//	Integer startIndex = 0;
//	String jsonName = "output.2018-08-28.egroup";	// Get All Retrieve Data
//	while(true) {
//		long startTime = System.currentTimeMillis();
//		faceList = getAllResult(ENGINEPATH,jsonName ,startIndex);
//		if(faceList.size()>0){
//			startIndex = faceList.get(faceList.size()-1).getEndIndex();
//		}
//		System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,startIndex="+startIndex+",faceList="+new Gson().toJson(faceList));
//		// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame = 100 ms
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	// Stop by yourself

	/**
	 * Get Retrieve result json
	 * 
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */
	public static List<Face> getAllResult(String jsonPath, String jsonName, int startIndex) {
		// init func
		final Gson gson = new Gson();
		final CopyUtil copyUtil = new CopyUtil();

		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		List<Face> faceList = new ArrayList<Face>();

		// Get retrieve result
		final File sourceJson = new File(jsonPath.toString() + "/" + jsonName + ".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/" + jsonName + "_copy.json");
		final File destJson = new File(jsonFileName.toString());
		if (sourceJson.exists() && sourceJson.length() != destJson.length()) {
			try {
				copyUtil.copyFile(sourceJson, destJson);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final File jsonFile = new File(jsonFileName.toString());
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;

			// If json exists
			if (jsonFile.exists()) {
				try {
					fileReader = new FileReader(jsonFileName.toString());
				} catch (FileNotFoundException e) {
				}
				bufferedReader = new BufferedReader(fileReader);
				// Read Json
				final StringBuilder jsonContent = new StringBuilder();
				String line;
				try {
					// Read line
					line = bufferedReader.readLine();
					while (line != null) {
						jsonContent.append(line + "\n");
						line = bufferedReader.readLine();
					}
					// If has data
					if (jsonContent.toString() != null) {
						// Get last one object
						int endIndex = jsonContent.lastIndexOf("}\n\t,");
						// System.out.println(endIndex);
						String json;
						// Reorganization json
						if (endIndex != -1 && startIndex != endIndex && startIndex < endIndex) {
							if (startIndex > 0) { // 從 JSON 內容 中間字元開始 [+ 擷取後JSON內容 +}]
								json = "[" + jsonContent.toString().substring(startIndex + 2, endIndex) + "}]";
							} else { // 從 JSON 內容 第一個字元開始 擷取後 JSON 內容 + }]
								json = jsonContent.toString().substring(startIndex, endIndex) + "}]";
							}
							System.out.println("json=" + json);
							faceList = gson.fromJson(json, faceListType);
							faceList.get(faceList.size() - 1).setEndIndex(endIndex + 2);
						}
					}
				} catch (IOException e) {
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return faceList;
	}

	/**
	 * Get Retrieve result json
	 * 
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */
	public static List<Face> getCacheResult(String jsonPath, String jsonName) {
		// init func
		final Gson gson = new Gson();
//		final CopyUtil copyUtil = new CopyUtil();

		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		List<Face> faceList = new ArrayList<Face>();

		// Get retrieve result
//		final File sourceJson = new File(jsonPath.toString() + "/" + jsonName + ".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/" + jsonName + ".json");// copy拿掉了

		// 拿掉copy檔
//		final File destJson = new File(jsonFileName.toString());
//		if (sourceJson.exists() && sourceJson.length() != destJson.length()) {
//			try {
//				copyUtil.copyFile(sourceJson, destJson);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		final File jsonFile = new File(jsonFileName.toString());
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		// If json exists
		if (jsonFile.exists()) {
			try {
				fileReader = new FileReader(jsonFileName.toString());
			} catch (FileNotFoundException e) {
			}
			bufferedReader = new BufferedReader(fileReader);
			// Read Json
			final StringBuilder jsonContent = new StringBuilder();
			String line;
			try {
				// Read line
				line = bufferedReader.readLine();
				while (line != null) {
					jsonContent.append(line + "\n");
					line = bufferedReader.readLine();
				}
				// If has data
				if (jsonContent.toString() != null) {
					// Get last one object
//						final int endIndex = jsonContent.lastIndexOf("}\n\t,");
//						final String json = jsonContent.toString().substring(0, endIndex) + "}]";
					faceList = gson.fromJson(jsonContent.toString(), faceListType);
					System.out.println("json=" + jsonContent.toString());

				}
				for (int i = 0; i < faceList.size(); i++) {
					int hasFound = Integer.valueOf(faceList.get(i).getHasFound());
					if (hasFound == 1) {
						System.out.println("HasFound : " + faceList.get(i).getHasFound());
						System.out.println(faceList.get(i).getPersonId());
						System.out.println(faceList.get(i).getImageSourcePath());

					} else {
						System.out.println("HasFound : " + faceList.get(i).getHasFound());
						System.out.println(faceList.get(i).getPersonId());
						System.out.println(faceList.get(i).getImageSourcePath());
					}
				}

			} catch (IOException e) {
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
		}

		return faceList;
	}

	// private static UserRepository userRepository;

//	public static Photo engineTag(String personId, String imageSourcePath) {
//		System.out.println("12");
//		return photoRepository.findByPhotoPath(imageSourcePath).map(photo -> {
//			System.out.println("123");
//			String photoId = photo.getId();
//			User users = new User(personId);
//			Photo photos = new Photo(photoId);
//			photos.getUser().add(users);
//			return photoRepository.save(photos);
//
//		}).orElseThrow(() -> new BadRequestException("photoId not found"));
////		User username = userRepository.findByUsername(personId)
////				.orElseThrow(() -> new AppException("username not set."));
//
//	}

}
