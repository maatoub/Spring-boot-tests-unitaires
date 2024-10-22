package com.nasr.project_tests_unitaires.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;
import com.nasr.project_tests_unitaires.exceptions.ResourceNotFoundException;
import com.nasr.project_tests_unitaires.exceptions.ValidationException;
import com.nasr.project_tests_unitaires.mapper.UtilisateurMapper;
import com.nasr.project_tests_unitaires.model.Utilisateur;
import com.nasr.project_tests_unitaires.repository.UtilisateurRepository;
import com.nasr.project_tests_unitaires.services.IUtilisateurService;

@Service
@Transactional
public class UtilisateurServiceImpl implements IUtilisateurService {

    private UtilisateurRepository repository;

    private UtilisateurMapper mapper;

    public UtilisateurServiceImpl(UtilisateurRepository repository, UtilisateurMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UtilisateurDTO addNewUser(UtilisateurDTO dto) {
        if (dto == null)
            throw new ValidationException("Utilisateur is null");
        boolean emailPresent = repository.findByEmail(dto.getEmail()).isPresent();
        if (emailPresent) {
            throw new ValidationException("Email already present");
        }
        Utilisateur objectSaved = repository.save(mapper.toUtilisateur(dto));
        return mapper.toUtilisateurDTO(objectSaved);
    }

    @Override
    public UtilisateurDTO updateUser(UtilisateurDTO dto) {
        if (dto == null)
            throw new ValidationException("Utilisateur is null");
        Utilisateur objectSaved = repository.save(mapper.toUtilisateur(dto));
        return mapper.toUtilisateurDTO(objectSaved);
    }

    @Override
    public UtilisateurDTO getUser(long id) {
        Utilisateur utilisateur = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        return mapper.toUtilisateurDTO(utilisateur);
    }

    @Override
    public void deleteUser(long id) {
        Utilisateur utilisateur = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        repository.delete(utilisateur);
    }

    @Override
    public List<UtilisateurDTO> getAllUsers() {
        return mapper.toListUtilisateursDTO(repository.findAll());
    }

    @Override
    public List<UtilisateurDTO> searchUser(String keyword) {
        List<Utilisateur> utilisateurs = repository.findByNomContainsIgnoreCase(keyword);
        return mapper.toListUtilisateursDTO(utilisateurs);
    }

}
