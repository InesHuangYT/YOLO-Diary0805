package com.example.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
//https://www.cnblogs.com/hbb0b0/p/8562277.html
//signup
//	filter0
//	filter3:null
//	filter1
//login	
//	filter0
//	filter3:Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0
//	filter1

	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			System.out.println("filter0");

			String jwt = getJwtFromRequest(request);
			System.out.println("filter1");
			System.out.println(jwt);
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				String userId = tokenProvider.getUserIdFromJWT(jwt);
				System.out.println(userId);// fatheer
				System.out.println("filter2");

				UserDetails userDetails = customUserDetailsService.loadUserById(userId);
				System.out.println(customUserDetailsService.loadUserById(userId));
				// com.example.security.UserPrincipal@bfd52522

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		System.out.println("filter3:" + bearerToken);
		// filter3:Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			System.out.println("filter4");

			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}
