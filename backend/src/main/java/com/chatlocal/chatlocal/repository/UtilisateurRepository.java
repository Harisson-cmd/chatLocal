package com.chatlocal.chatlocal.repository;


import com.chatlocal.chatlocal.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    boolean existsByIdentifiant(String identifiant);

    Optional<Utilisateur> findByIdentifiant(String identifiant);
}