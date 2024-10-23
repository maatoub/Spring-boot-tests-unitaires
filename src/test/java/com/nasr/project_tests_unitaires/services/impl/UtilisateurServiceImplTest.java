package com.nasr.project_tests_unitaires.services.impl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;
import com.nasr.project_tests_unitaires.exceptions.ResourceNotFoundException;
import com.nasr.project_tests_unitaires.exceptions.ValidationException;
import com.nasr.project_tests_unitaires.mapper.UtilisateurMapper;
import com.nasr.project_tests_unitaires.model.Utilisateur;
import com.nasr.project_tests_unitaires.repository.UtilisateurRepository;

@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceImplTest {

    @Mock
    private UtilisateurRepository repositoryMock;

    @Mock
    private UtilisateurMapper mapperMock;

    @InjectMocks
    private UtilisateurServiceImpl underTest;

    private Utilisateur utilisateur;
    private UtilisateurDTO utilisateurDTO;
    private List<Utilisateur> utilisateurs;

    @BeforeEach
    void setUp() {
        utilisateurDTO = UtilisateurDTO.builder().nom("Nasser").email("nasser@gmail.com").build();
        utilisateur = Utilisateur.builder().nom("Nasser").email("nasser@gmail.com").build();

        utilisateurs = List.of(
                Utilisateur.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build(),
                Utilisateur.builder().id(2L).nom("Khalid").email("khalid@gmail.com").build());
    }

    @Test
    void testAddNewUser() {

        Utilisateur savedUtilisateur = Utilisateur.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build();
        UtilisateurDTO expected = UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build();

        Mockito.when(repositoryMock.findByEmail(utilisateurDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(mapperMock.toUtilisateur(utilisateurDTO)).thenReturn(utilisateur);
        Mockito.when(repositoryMock.save(utilisateur)).thenReturn(savedUtilisateur);
        Mockito.when(mapperMock.toUtilisateurDTO(savedUtilisateur)).thenReturn(expected);
        UtilisateurDTO result = underTest.addNewUser(utilisateurDTO);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void notSaveUserWhenEmailExist() {

        Mockito.when(repositoryMock.findByEmail(utilisateurDTO.getEmail())).thenReturn(Optional.of(utilisateur));
        assertThatThrownBy(() -> underTest.addNewUser(utilisateurDTO)).isInstanceOf(ValidationException.class);
    }

    @Test
    void testDeleteUser() {
        long utilisateurId = 6L;
        Mockito.when(repositoryMock.findById(utilisateurId)).thenReturn(Optional.of(utilisateur));
        underTest.deleteUser(utilisateurId);
        Mockito.verify(repositoryMock).delete(utilisateur);
    }

    @Test
    void testGetAllUsers() {

        List<UtilisateurDTO> expected = List.of(
                UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build(),
                UtilisateurDTO.builder().id(1L).nom("Khalid").email("khalid@gmail.com").build());

        Mockito.when(repositoryMock.findAll()).thenReturn(utilisateurs);
        Mockito.when(mapperMock.toListUtilisateursDTO(utilisateurs)).thenReturn(expected);
        List<UtilisateurDTO> result = underTest.getAllUsers();

        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testGetUser() {
        long userId = 1L;
        UtilisateurDTO expected = UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build();

        Mockito.when(repositoryMock.findById(userId)).thenReturn(Optional.of(utilisateur));
        Mockito.when(mapperMock.toUtilisateurDTO(utilisateur)).thenReturn(expected);
        UtilisateurDTO result = underTest.getUser(userId);

        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testUserNotFound() {
        long userId = 1L;
        Mockito.when(repositoryMock.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getUser(userId)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testSearchUser() {
        String keyword = "nass";

        List<UtilisateurDTO> expected = List.of(
                UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build());

        Mockito.when(repositoryMock.findByNomContainsIgnoreCase(keyword)).thenReturn(utilisateurs);
        Mockito.when(mapperMock.toListUtilisateursDTO(utilisateurs)).thenReturn(expected);

        List<UtilisateurDTO> result = underTest.searchUser(keyword);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testUpdateUser() {
        long customerId = 1L;
        utilisateur = Utilisateur.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build();
        utilisateurDTO = UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build();

        Utilisateur updateUtilisateur = Utilisateur.builder().id(1L).nom("Nasser").email("nasser14@gmail.com").build();
        UtilisateurDTO expected = UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser14@gmail.com").build();

        Mockito.when(repositoryMock.findById(customerId)).thenReturn(Optional.of(utilisateur));
        Mockito.when(mapperMock.toUtilisateur(utilisateurDTO)).thenReturn(utilisateur);
        Mockito.when(repositoryMock.save(utilisateur)).thenReturn(updateUtilisateur);
        Mockito.when(mapperMock.toUtilisateurDTO(updateUtilisateur)).thenReturn(expected);

        UtilisateurDTO result = underTest.updateUser(utilisateurDTO);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }
}
