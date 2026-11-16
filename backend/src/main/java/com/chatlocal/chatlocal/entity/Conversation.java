package com.chatlocal.chatlocal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/** Conversation : classe mère (ConversationPrivee maintenant, Groupe plus tard). */
@Entity
@Table(name = "conversation")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_conversation", length = 20)
public abstract class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    /** Sert à trier la liste des conversations par activité récente. */
    @Column(nullable = false)
    private LocalDateTime dateDerniereActivite = LocalDateTime.now();

    /** Association "participe à" : les utilisateurs de la conversation (2 pour une privée). */
    @ManyToMany
    @JoinTable(name = "conversation_participant",
               joinColumns = @JoinColumn(name = "conversation_id"),
               inverseJoinColumns = @JoinColumn(name = "utilisateur_id"))
    private Set<Utilisateur> participants = new HashSet<>();

    public Long getId() { return id; }
    public LocalDateTime getDateCreation() { return dateCreation; }

    public LocalDateTime getDateDerniereActivite() { return dateDerniereActivite; }
    public void setDateDerniereActivite(LocalDateTime d) { this.dateDerniereActivite = d; }

    public Set<Utilisateur> getParticipants() { return participants; }
}
