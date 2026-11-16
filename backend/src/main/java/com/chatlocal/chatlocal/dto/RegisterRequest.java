package com.chatlocal.chatlocal.dto;


import com.chatlocal.chatlocal.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String identifiant,
        @NotBlank @Size(min = 8, max = 100) String motDePasse,
        @NotBlank String confirmationMotDePasse,
        @NotBlank @Size(max = 100) String nom,
        @NotBlank @Size(max = 100) String prenom,
        @Size(max = 100) String filiere,
        @Size(max = 50) String niveau,
        @NotNull Role role
) {
}
