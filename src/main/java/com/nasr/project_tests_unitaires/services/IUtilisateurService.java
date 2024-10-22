package com.nasr.project_tests_unitaires.services;

import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;
import java.util.List;

public interface IUtilisateurService {
    UtilisateurDTO addNewUser(UtilisateurDTO utilisateur);

    UtilisateurDTO updateUser(UtilisateurDTO utilisateur);

    UtilisateurDTO getUser(long id);

    void deleteUser(long id);

    List<UtilisateurDTO> searchUser(String keyword);

    List<UtilisateurDTO> getAllUsers();

}
