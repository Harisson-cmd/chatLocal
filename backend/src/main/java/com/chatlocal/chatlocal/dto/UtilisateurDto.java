package com.chatlocal.chatlocal.dto;

import com.chatlocal.chatlocal.entity.Role;
import com.chatlocal.chatlocal.entity.StatutCompte;
import com.chatlocal.chatlocal.entity.Utilisateur;

public record UtilisateurDto(
        Long id,
        String identifiant,
        String nom,
        String prenom,
        String filiere,
        String niveau,
        Role role,
        StatutCompte statut
) {
    public static UtilisateurDto from(Utilisateur u) {
        return new UtilisateurDto(u.getId(), u.getIdentifiant(), u.getNom(), u.getPrenom(),
                u.getFiliere(), u.getNiveau(), u.getRole(), u.getStatut());
    }
}
