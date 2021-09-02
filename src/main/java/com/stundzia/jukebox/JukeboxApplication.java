package com.stundzia.jukebox;

import com.stundzia.jukebox.components.CardComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = "com.stundzia.jukebox")
public class JukeboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JukeboxApplication.class, args);
	}

	@Autowired
	CardComponent cardComponent;

	@PostConstruct
	public void init() {
		cardComponent.startScanner();
	}

	@Bean
	public SpringDataDialect springDataDialect() {
		return new SpringDataDialect();
	}

}
