package com.nasr.project_tests_unitaires.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {
    private Long id;

    @Size(min = 2)
    @NotNull(message = "Nom is required")
    private String nom;

    @Size(min = 6)
    @NotNull(message = "Email is required")
    private String email;
}
