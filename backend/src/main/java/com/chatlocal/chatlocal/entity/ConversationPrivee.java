package com.chatlocal.chatlocal.entity;

import jakarta.persistence.*;

/** Conversation entre deux utilisateurs (les participants sont dans Conversation). */
@Entity
@DiscriminatorValue("PRIVEE")
public class ConversationPrivee extends Conversation {

    /** Identifie la paire, ex. "3-7". UNIQUE en base : impossible de créer deux conversations pour la même paire. */
    @Column(length = 40)
    private String clePrivee;

    protected ConversationPrivee() {
    }

    /** Les deux utilisateurs doivent déjà être enregistrés (avoir un id). */
    public ConversationPrivee(Utilisateur a, Utilisateur b) {
        getParticipants().add(a);
        getParticipants().add(b);
        this.clePrivee = cle(a.getId(), b.getId());
    }

    /** Même clé quel que soit l'ordre : cle(3,7) = cle(7,3) = "3-7". */
    public static String cle(Long idA, Long idB) {
        return Math.min(idA, idB) + "-" + Math.max(idA, idB);
    }

    public String getClePrivee() { return clePrivee; }
}
