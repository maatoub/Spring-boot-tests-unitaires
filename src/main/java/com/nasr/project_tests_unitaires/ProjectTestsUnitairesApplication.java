package com.nasr.project_tests_unitaires;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.nasr.project_tests_unitaires.model.Utilisateur;
import com.nasr.project_tests_unitaires.repository.UtilisateurRepository;

@SpringBootApplication
public class ProjectTestsUnitairesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTestsUnitairesApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner runner(UtilisateurRepository repo) {
		return args -> {
			repo.save(Utilisateur.builder()
					.email("nasr@email.com").nom("Nasser").build());

			repo.save(Utilisateur.builder()
					.email("yassine@email.com").nom("Yassine").build());

			repo.save(Utilisateur.builder()
					.email("ikram@email.com").nom("Ikram").build());
		};
	}

}
