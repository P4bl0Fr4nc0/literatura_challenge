package com.example.literatura_challenge;

import com.example.literatura_challenge.principal.Principal;
import com.example.literatura_challenge.repository.AutorRepository;
import com.example.literatura_challenge.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraturaChallengeApplication implements CommandLineRunner {

	@Autowired
	private LibrosRepository repository;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaChallengeApplication.class, args);

	}

	@Override

	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository,autorRepository);
		principal.menu();
	}





}
