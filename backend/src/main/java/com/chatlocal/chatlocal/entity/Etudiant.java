package com.chatlocal.chatlocal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "etudiant")
public class Etudiant extends Utilisateur {

    public Etudiant() {
        super(Role.ETUDIANT);
    }
}