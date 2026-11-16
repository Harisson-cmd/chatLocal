package com.chatlocal.chatlocal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrateur")
public class Administrateur extends Utilisateur {

    public Administrateur() {
        super(Role.ADMINISTRATEUR);
    }
}