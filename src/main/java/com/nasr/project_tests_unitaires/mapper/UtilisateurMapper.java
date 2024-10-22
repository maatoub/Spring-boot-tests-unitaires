package com.nasr.project_tests_unitaires.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;
import com.nasr.project_tests_unitaires.model.Utilisateur;

@Service
public class UtilisateurMapper {

    ModelMapper mapper = new ModelMapper();

    public Utilisateur toUtilisateur(UtilisateurDTO dto) {
        return mapper.map(dto, Utilisateur.class);
    }

    public UtilisateurDTO toUtilisateurDTO(Utilisateur utilisateur) {
        return mapper.map(utilisateur, UtilisateurDTO.class);
    }

    public List<UtilisateurDTO> toListUtilisateursDTO(List<Utilisateur> utilisateurs) {
        return utilisateurs.stream().map((item) -> mapper.map(
                item, UtilisateurDTO.class))
                .collect(Collectors.toList());
    }
}
