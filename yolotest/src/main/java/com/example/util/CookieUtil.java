package com.example.util;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
@Service
public class CookieUtil {
	public void setLoginTokenCookie(String jwt, HttpServletResponse response) {
		setCookie("loginToken",jwt,response);
		
	}

	public void setCookie(String name, String value, HttpServletResponse response) {
		// save Cookie
		final Cookie cookie_tid = new Cookie(name, value);
		cookie_tid.setMaxAge(60 * 60 * 24); // 24HR
		cookie_tid.setPath("/");
		// cookie_tid.setSecure(true);
		response.addCookie(cookie_tid);
	}

	public String getCookie(HttpServletRequest request, String cookieName) {
		final Cookie[] cookies = request.getCookies();
		final HashMap<String, String> cookieHashmap = new HashMap<>();
		System.out.println("cookies: "+ cookies.toString());
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookieHashmap.put(cookies[i].getName(), cookies[i].getValue());
			}
			System.out.println("cookieMAP: "+ cookieHashmap);
			return cookieHashmap.get(cookieName.toString());
		}
		return null;
	}

	//清除Cookie
		public boolean cleanCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
			final Cookie[] cookies = request.getCookies();
			if(cookies!=null){
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals(cookieName)){
						cookies[i].setMaxAge(0);
						cookies[i].setPath("/");
						response.addCookie(cookies[i]);
						return true;
					}
				}
			}
			return false;
		}
}
