package com.nasr.project_tests_unitaires.web;

import static org.mockito.Mockito.doNothing;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;
import com.nasr.project_tests_unitaires.services.IUtilisateurService;

@ActiveProfiles("test")
@WebMvcTest(UtilisateurController.class)
public class UtilisateurControllerTest {

    @MockBean
    private IUtilisateurService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private List<UtilisateurDTO> utilisateursDTO;

    @BeforeEach
    void setUp() {
        this.utilisateursDTO = List.of(
                UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build(),
                UtilisateurDTO.builder().id(2L).nom("Khalid").email("khalid@gmail.com").build());
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(service).deleteUser(userId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(service.getAllUsers()).thenReturn(utilisateursDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(utilisateursDTO)));
    }

    @Test
    void testGetUser() throws Exception {
        Long userId = 2L;

        Mockito.when(service.getUser(userId)).thenReturn(utilisateursDTO.get(1));
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(utilisateursDTO.get(1))));
    }

    @Test
    void testSaveUser() throws Exception {
        UtilisateurDTO newUser = UtilisateurDTO.builder().nom("Nasser").email("nasser@gmail.com").build();
        Mockito.when(service.addNewUser(newUser)).thenReturn(utilisateursDTO.get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/save", newUser)
                .contentType("application/json")
                .content(mapper.writeValueAsString(newUser)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(utilisateursDTO.get(0))));
    }

    @Test
    void testSearchUser() throws Exception {
        String keyword = "nas";
        List<UtilisateurDTO> expected = List.of(
                UtilisateurDTO.builder().id(1L).nom("Nasser").email("nasser@gmail.com").build());

        Mockito.when(service.searchUser(keyword)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/users").param("keyword", keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void testUpdateUser() throws JsonProcessingException, Exception {
        UtilisateurDTO updateUser = UtilisateurDTO.builder().id(1L).nom("Nasr").email("nasser.86@gmail.com").build();
        Mockito.when(service.updateUser(utilisateursDTO.get(0))).thenReturn(updateUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update")
                .contentType("application/json")
                .content(mapper.writeValueAsString(updateUser)))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }
}
