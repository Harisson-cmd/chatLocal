package com.chatlocal.chatlocal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "enseignant")
public class Enseignant extends Utilisateur {

    private boolean estDelegue = false;

    public Enseignant() {
        super(Role.ENSEIGNANT);
    }

    public boolean isEstDelegue() { return estDelegue; }
    public void setEstDelegue(boolean estDelegue) { this.estDelegue = estDelegue; }
}