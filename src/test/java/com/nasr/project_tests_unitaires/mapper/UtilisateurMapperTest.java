package com.nasr.project_tests_unitaires.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;
import com.nasr.project_tests_unitaires.model.Utilisateur;

public class UtilisateurMapperTest {

    UtilisateurMapper underTest;

    @BeforeEach
    public void setUp() {
        underTest = new UtilisateurMapper();
    }

    @Test
    void testToUtilisateurDTO() {
        Utilisateur givenUtilisateur = Utilisateur.builder().id(1L).nom("Med").email("med@gmail.com").build();
        UtilisateurDTO result = underTest.toUtilisateurDTO(givenUtilisateur);
        assertThat(result).isNotNull();
        assertThat(givenUtilisateur).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void testToUtilisateur() {
        UtilisateurDTO givenUtilisateurDTO = UtilisateurDTO.builder().id(1L).nom("Med").email("med@gmail.com").build();
        Utilisateur result = underTest.toUtilisateur(givenUtilisateurDTO);
        assertThat(result).isNotNull();
        assertThat(givenUtilisateurDTO).usingRecursiveComparison().isEqualTo(result);

    }

    @Test
    void testToListUtilisateursDTO() {
        List<Utilisateur> givenUtilisateurs = List.of(
                Utilisateur.builder().id(1L).nom("Said").email("said@gmail.com").build(),
                Utilisateur.builder().id(2L).nom("Khalid").email("khalid@gmail.com").build());

        List<UtilisateurDTO> result = underTest.toListUtilisateursDTO(givenUtilisateurs);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(givenUtilisateurs.size());
        assertThat(result).usingRecursiveComparison().isEqualTo(givenUtilisateurs);
    }

    @Test
    void shouldNotMapNullValue() {
        Utilisateur given = null;
        assertThatThrownBy(() -> underTest.toUtilisateurDTO(given)).isInstanceOf(IllegalArgumentException.class);
    }

}
