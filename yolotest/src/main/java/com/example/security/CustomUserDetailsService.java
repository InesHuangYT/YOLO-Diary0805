package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.User;
import com.example.repository.UserRepository;
/* 這個接口定義了一個方法 loadUserByUsername
 * 提供一種從用户名可以查到用户並返回的方法。
 * 注意，不一定是資料庫，文本文件、xml文件等等都可能成為資料源，
 * 這也是為什麼Spring提供這樣一個接口的原因：保證你可以採用靈活的資料源。
 * 建立一個 CustomUserDetailsService 来實現這個接口。
*/
@Service

public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// Let people login with either username or email
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));

		return UserPrincipal.create(user);
	}

	//used by JWTAuthenticationFilter
	//注意
	@Transactional
	public UserDetails loadUserById(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + username));

		return UserPrincipal.create(user);
	}

}
