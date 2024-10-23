package com.nasr.project_tests_unitaires.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nasr.project_tests_unitaires.dto.UtilisateurDTO;
import com.nasr.project_tests_unitaires.services.IUtilisateurService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    private IUtilisateurService service;

    public UtilisateurController(IUtilisateurService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<UtilisateurDTO> saveUser(@RequestBody UtilisateurDTO dto) {
        return new ResponseEntity<>(service.addNewUser(dto), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UtilisateurDTO> updateUser(@RequestBody UtilisateurDTO dto) {
        return new ResponseEntity<>(service.updateUser(dto), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(service.getUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> DeleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UtilisateurDTO>> getAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UtilisateurDTO>> searchUser(@RequestParam String keyword) {
        return new ResponseEntity<>(service.searchUser(keyword), HttpStatus.OK);
    }
}
