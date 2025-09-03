package com.example.VolunteerHub;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class VolunteerHubApplication {
	@PostConstruct
	public void init() {
		// Ép JVM luôn chạy theo giờ Việt Nam
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		System.out.println(">>> Application timezone = " + TimeZone.getDefault().getID());
	}

	public static void main(String[] args) {
		SpringApplication.run(VolunteerHubApplication.class, args);
	}

}
