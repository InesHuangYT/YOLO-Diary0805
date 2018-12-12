package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;

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
		mail.setText("謝謝您，點擊網址後即註冊成功" + "thank you a lot !");

		javaMailSender.send(mail);
	}

	public void sendInviteNotification(String email, String username) throws MailException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(email);
		mail.setFrom("Yolo8Diary@gmail.com");
		mail.setSubject("YOLO Diary 用戶邀請函 ");
		mail.setText("親愛的用戶你/妳好：\n"+
		"你/妳已被 " + username + " 用戶邀請來一同參與憂樂日記。\n"+
				"來一起與親朋好友們創造屬於你們獨特的共享回憶吧！\n\n\n\n"+ "\t\t\t\tYOLO Diary憂樂日記");

		javaMailSender.send(mail);

	}
	
	public void sendTageNotification(Optional<User> user, @CurrentUser UserPrincipal currentuser) {
		
		SimpleMailMessage mail = new SimpleMailMessage(); 
		mail.setTo(user.get().getEmail());
		mail.setFrom("Yolo8Diary@gmail.com");
		mail.setSubject("YOLO Diary 標記通知 ");
		mail.setText("親愛的用戶你/妳好：\n"+
				"你/妳已被 " + currentuser.getUsername() + " 用戶標記在 "+"0000泡泡日記" + "。"+"\n"+
						"來一起與親朋好友們創造屬於你們獨特的共享回憶吧！\n\n\n\n"+ "\t\t\t\tYOLO Diary憂樂日記");
		System.out.println("|Check User|"+user.get().getEmail());
	}

}
