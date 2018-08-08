package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.entity.User;

@Service
public class NotificationService {
	private JavaMailSender javaMailSender;
	
	@Autowired
	public NotificationService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender; 
		
 		
	}
	
	public void sendNotification(User user) throws MailException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom("Yolo8Diary@gmail.com");
		mail.setSubject("YOLO Diary 驗證請求");
		mail.setText("謝謝您，點擊網址後即註冊成功"
				+ "thank you a lot !");
		
		
		javaMailSender.send(mail);
	}

}
