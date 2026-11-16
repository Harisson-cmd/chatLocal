package com.chatlocal.chatlocal.repository;

import com.chatlocal.chatlocal.entity.*;

/** Jeu de données de test : 2 étudiants, 1 enseignant, 1 administrateur. */
public class JeuDeDonnees {

    public Utilisateur alice, bob, prof, admin;

    public JeuDeDonnees(UtilisateurRepository repo) {
        alice = repo.save(etudiant("alice", "Alice Ngo"));
        bob = repo.save(etudiant("bob", "Bob Essomba"));

        Enseignant e = new Enseignant();
        remplir(e, "prof1", "Dr Mandebi");
        e.setEstDelegue(true);
        prof = repo.save(e);

        Administrateur a = new Administrateur();
        remplir(a, "admin", "Administrateur");
        admin = repo.save(a);
    }

    private static Etudiant etudiant(String identifiant, String nom) {
        Etudiant e = new Etudiant();
        remplir(e, identifiant, nom);
        e.setFiliere("GISLI");
        e.setNiveau("L3");
        return e;
    }

    private static void remplir(Utilisateur u, String identifiant, String nom) {
        u.setIdentifiant(identifiant);
        u.setMotDePasse("hache-factice"); // en vrai : un hash BCrypt
        u.setNom(nom);
    }
}
