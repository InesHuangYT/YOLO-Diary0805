package com.example.engine.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.example.security.CurrentUser;

//建立檔案
//http://blog.xuite.net/sea54100/wretch1/122337059-JAVA+-+%E5%BB%BA%E7%AB%8B%E8%B3%87%E6%96%99%E5%A4%BE%E8%B7%9F%E6%AA%94%E6%A1%88

//成功將文字寫入檔案
//https://blog.johnsonlu.org/java%E8%AE%80%E5%8F%96%E8%88%87%E5%AF%AB%E5%85%A5%E6%AA%94%E6%A1%88/

//寫入文字檔且不會覆蓋之前的內容
//https://www.cnblogs.com/wmxz/archive/2013/03/27/2985308.html

//記事本文字換行，為什麼要用/r/n?
//http://catchtest.pixnet.net/blog/post/21981758-java%E7%9A%84%E6%8F%9B%E8%A1%8C%E7%AC%A6%E8%99%9F
public class Textfile {
//	public static void write(String photoPath, @CurrentUser String current, int i){
//	     
//try {
//	File file = new File("C:\\Users\\Administrator\\Desktop\\sample.txt");
//	file.createNewFile();

//	FileWriter fw = new FileWriter("C:\\Users\\Administrator\\Desktop\\Engine0818\\list.txt", true);
//	fw.write(photoPath+i+".jpg"+"\t"+current+"[NO]"+i+"\r\n");
//	fw.flush();
//	fw.close();
//	
//}catch(Exception e) {
//	System.out.println("FAIL");
//}
//}
	/**我新增getphotopath，把原本的getphotopath改成getselfiepath了**/
	
	public static void getselfiepath(String diretorypath, @CurrentUser String current) throws IOException {
		File file = new File(diretorypath);
		File[] filearray = file.listFiles();
		FileWriter fw = new FileWriter("C:\\eGroupAI_FaceRecognitionEngine_V3.0\\listSelfie.txt", true);
		// C:\\Users\\Administrator\\Desktop\\Engine0818\\list.txt --> rrou's path
		// C:\\eGroupAI_FaceRecognitionEngine_V3.0\\list.txt --> ines's path

		for (int i = 0; i < filearray.length; i++) {
			fw.write(filearray[i] + "\t" + current + "[No]" + i + "\r\n");
			fw.flush();
			System.out.println(filearray[i]);
		}
		// close filewriter
		// https://stackoverflow.com/questions/22900477/java-io-exception-stream-closed
		fw.close();

	}

	public static void getphotopath(String diretorypath, Long diaryId) throws IOException {
		File file = new File(diretorypath);
		File[] filearray = file.listFiles();
		FileWriter fw = new FileWriter("C:\\eGroupAI_FaceRecognitionEngine_V3.0\\photolist.egroupList", true);
		// C:\\Users\\Administrator\\Desktop\\Engine0818\\list.txt --> rrou's path
		// C:\\eGroupAI_FaceRecognitionEngine_V3.0\\list.txt --> ines's path

		for (int i = 0; i < filearray.length; i++) {
			fw.write(filearray[i] + "\r\n");
			fw.flush();
			System.out.println(filearray[i]);
		}
		// close filewriter
		// https://stackoverflow.com/questions/22900477/java-io-exception-stream-closed
		fw.close();

	}

}