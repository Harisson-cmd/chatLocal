package com.chatlocal.chatlocal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String identifiant,
        @NotBlank @Size(min = 8, max = 100) String motDePasse,
        @NotBlank @Size(max = 100) String nom,
        @Size(max = 100) String filiere,
        @Size(max = 50) String niveau
) {
}