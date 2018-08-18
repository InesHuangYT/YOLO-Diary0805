package com.example.config;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.security.UserPrincipal;

@Configuration
@EnableJpaAuditing

//要自動填充createdBy和updatedBy字段 change to all string

public class AuditingConfig {
	@Bean
	public AuditorAware<String> auditorProvider(){
		return new SpringSecurityAuditImpl();
	}
	
}
class SpringSecurityAuditImpl implements AuditorAware<String>{
	
	@Override
	public Optional<String> getCurrentAuditor(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication==null||!authentication.isAuthenticated()||authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return Optional.ofNullable(userPrincipal.getUsername());
	}
}