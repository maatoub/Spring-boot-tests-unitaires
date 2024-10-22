package com.nasr.project_tests_unitaires.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nasr.project_tests_unitaires.model.Utilisateur;
import java.util.Optional;
import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByNomContainsIgnoreCase(String keyword);
}
