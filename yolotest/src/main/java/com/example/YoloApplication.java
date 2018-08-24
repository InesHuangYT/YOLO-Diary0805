package com.example;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;




//(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})

//@EnableJpaAuditing

@EntityScan(basePackageClasses = { 
		YoloApplication.class,
		Jsr310JpaConverters.class 
})
@SpringBootApplication
@ComponentScan({"com.example.engine.controller", "com.example.engine.entity", "com.example.engine.util"})
public class YoloApplication {
	
	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));
	}
	
	public static void main(String[] args) {
		System.out.println("bonjour");
		SpringApplication.run(YoloApplication.class, args);
	}

		}
