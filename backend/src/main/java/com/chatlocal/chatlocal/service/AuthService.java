package com.chatlocal.chatlocal.service;

import com.chatlocal.chatlocal.dto.RegisterRequest;
import com.chatlocal.chatlocal.dto.UtilisateurDto;
import com.chatlocal.chatlocal.entity.Etudiant;
import com.chatlocal.chatlocal.entity.Utilisateur;
import com.chatlocal.chatlocal.exception.ApiException;
import com.chatlocal.chatlocal.repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inscription publique : le compte créé est toujours un étudiant.
     * Les rôles enseignant et administrateur sont attribués plus tard par un administrateur.
     */
    @Transactional
    public UtilisateurDto inscrire(RegisterRequest req) {
        if (utilisateurRepository.existsByIdentifiant(req.identifiant())) {
            throw new ApiException(HttpStatus.CONFLICT, "Cet identifiant est déjà utilisé");
        }

        Utilisateur u = new Etudiant();
        u.setIdentifiant(req.identifiant());
        u.setMotDePasse(passwordEncoder.encode(req.motDePasse()));
        u.setNom(req.nom());
        u.setFiliere(req.filiere());
        u.setNiveau(req.niveau());

        return UtilisateurDto.from(utilisateurRepository.save(u));
    }
}