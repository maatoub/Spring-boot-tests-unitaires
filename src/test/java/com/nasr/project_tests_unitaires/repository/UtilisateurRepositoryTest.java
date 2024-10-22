package com.nasr.project_tests_unitaires.repository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.nasr.project_tests_unitaires.model.Utilisateur;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UtilisateurRepositoryTest {

    @Autowired
    private UtilisateurRepository repository;

    @Test
    void testFindByEmail() {
        String givenEmail = "nasr@email.com";
        Optional<Utilisateur> result = repository.findByEmail(givenEmail);
        AssertionsForClassTypes.assertThat(result).isPresent();
    }

    @Test
    void testFindByNomContainsIgnoreCase() {
        String givenNom = "ss";
        List<Utilisateur> result = repository.findByNomContainsIgnoreCase(givenNom);
        List<Utilisateur> expected = List.of(
                Utilisateur.builder()
                        .email("nasr@email.com").nom("Nasser").build(),
                Utilisateur.builder()
                        .email("yassine@email.com").nom("Yassine").build());

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

}
