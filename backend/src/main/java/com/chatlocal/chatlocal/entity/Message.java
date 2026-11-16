package com.chatlocal.chatlocal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** UUID créé par le client, jamais modifié : la base refuse deux messages avec le même idClient. */
    @Column(nullable = false, unique = true, length = 36)
    private String idClient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auteur_id")
    private Utilisateur auteur;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutMessage statut = StatutMessage.ENVOYE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    protected Message() {
    }

    public Message(String idClient, Conversation conversation, Utilisateur auteur, String contenu, Document document) {
        this.idClient = idClient;
        this.conversation = conversation;
        this.auteur = auteur;
        this.contenu = contenu;
        this.document = document;
    }

    public Long getId() { return id; }
    public String getIdClient() { return idClient; }
    public Conversation getConversation() { return conversation; }
    public Utilisateur getAuteur() { return auteur; }
    public String getContenu() { return contenu; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public Document getDocument() { return document; }

    public StatutMessage getStatut() { return statut; }
    public void setStatut(StatutMessage statut) { this.statut = statut; }
}
