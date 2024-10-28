package com.nasr.project_tests_unitaires.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UtilisateurIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    List<UtilisateurDTO> utilisateurDTOs;

    @BeforeEach
    void setUp() {
        this.utilisateurDTOs = List.of(
                UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasr@email.com").build(),
                UtilisateurDTO.builder().id(2L).nom("Yassine").email("yassine@email.com").build(),
                UtilisateurDTO.builder().id(3L).nom("Ikram").email("ikram@email.com").build());
    }

    @Test
    void shouldGetAllUsers() {
        ResponseEntity<UtilisateurDTO[]> response = restTemplate.exchange("/users/all", HttpMethod.GET, null,
                UtilisateurDTO[].class);
        List<UtilisateurDTO> content = Arrays.asList(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content.size()).isEqualTo(utilisateurDTOs.size());
        assertThat(content).usingRecursiveComparison().isEqualTo(utilisateurDTOs);
    }

    @Test
    void shouldSearchUsersByNom() {
        String keyword = "ss";
        List<UtilisateurDTO> expected = List.of(
                UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasr@email.com").build(),
                UtilisateurDTO.builder().id(2L).nom("Yassine").email("yassine@email.com").build());

        ResponseEntity<UtilisateurDTO[]> response = restTemplate.exchange("/users?keyword=" + keyword, HttpMethod.GET,
                null, UtilisateurDTO[].class);
        List<UtilisateurDTO> content = Arrays.asList(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content.size()).isEqualTo(2);
        assertThat(content).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldSaveUser() {
        UtilisateurDTO newUser = UtilisateurDTO.builder().nom("Sifaw").email("sifaw@email.com").build();

        ResponseEntity<UtilisateurDTO> response = restTemplate.exchange("/users/save", HttpMethod.POST,
                new HttpEntity<>(newUser), UtilisateurDTO.class);
        UtilisateurDTO content = response.getBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(content).usingRecursiveComparison().ignoringFields("id").isEqualTo(newUser);
    }

    @Test
    void shouldNotSaveInvalidUser() throws JsonMappingException, JsonProcessingException {
        UtilisateurDTO newUser = UtilisateurDTO.builder().nom("").email("").build();
        ResponseEntity<String> response = restTemplate.exchange("/users/save", HttpMethod.POST,
                new HttpEntity<>(newUser), String.class);
        Map<String, List<String>> errors = mapper.readValue(response.getBody(), HashMap.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errors.keySet().size()).isEqualTo(2);
        assertThat(errors.get("nom").size()).isEqualTo(2);
        assertThat(errors.get("email").size()).isEqualTo(2);
    }

    @Test
    void shouldGetUserById() {
        Long userId = 1L;

        ResponseEntity<UtilisateurDTO> response = restTemplate.exchange("/users/{id}", HttpMethod.GET, null,
                UtilisateurDTO.class, userId);
        UtilisateurDTO content = response.getBody();

        assertThat(content).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content).usingRecursiveComparison().isEqualTo(utilisateurDTOs.get(0));
    }

    @Test
    @Rollback
    void shouldDeleteUserById() {
        Long userId = 1L;
        ResponseEntity<UtilisateurDTO> response = restTemplate.exchange("/users/{id}", HttpMethod.DELETE, null,
                UtilisateurDTO.class, userId);
        UtilisateurDTO content = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content).isNull();
    }

    @Test
    @Rollback
    void shouldUpadateUser() {
        UtilisateurDTO expected = UtilisateurDTO.builder().id(1L).nom("nasru").email("nasru@gmail.com").build();
        ResponseEntity<UtilisateurDTO> response = restTemplate.exchange("/users/update", HttpMethod.PUT,
                new HttpEntity<>(expected), UtilisateurDTO.class);
        UtilisateurDTO content = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(content).usingRecursiveComparison().isEqualTo(expected);
    }
}