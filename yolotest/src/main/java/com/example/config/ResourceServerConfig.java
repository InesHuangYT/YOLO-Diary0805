package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
//	private static final String RESOURCE_ID = "my_rest_api";
//	
//	
//	@Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.resourceId(RESOURCE_ID).stateless(false);
//    }
// 

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		
//		 http.
//	        anonymous().disable()
//	        .requestMatchers().antMatchers("/user/**")
//	        .and().authorizeRequests()
//	        .antMatchers("/user/**").access("hasRole('ADMIN')")
//	        .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
		http.headers().frameOptions().disable().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()//https://www.jianshu.com/p/6307c89fe3fa
		.authorizeRequests()
		.antMatchers("/").permitAll()
		//.antMatchers("/api/auth/private**").access("hasRole('USER')")
		//.antMatchers("/api/auth/private**").permitAll()
		.antMatchers("/api/auth/login**").permitAll()
		//.antMatchers("/auth/**").permitAll()
        // 除上面外的所有请求全部需要鉴权认证
        .anyRequest().authenticated();
		

	}

}
