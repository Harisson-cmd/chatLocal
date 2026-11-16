package com.chatlocal.chatlocal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Fichier déposé par un utilisateur (le fichier est sur le disque ; ici on garde ses informations). */
@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomFichier;

    @Column(nullable = false, unique = true, length = 500)
    private String cheminStockage;

    @Column(nullable = false, length = 100)
    private String typeMime;

    @Column(nullable = false)
    private long tailleOctets;

    @Column(nullable = false)
    private LocalDateTime dateDepot = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auteur_id")
    private Utilisateur auteur;

    /** Réservé au classement par matière (plus tard) : reste vide pour l'instant. */
    private Long matiereId;

    protected Document() {
    }

    public Document(String nomFichier, String cheminStockage, String typeMime, long tailleOctets, Utilisateur auteur) {
        this.nomFichier = nomFichier;
        this.cheminStockage = cheminStockage;
        this.typeMime = typeMime;
        this.tailleOctets = tailleOctets;
        this.auteur = auteur;
    }

    public Long getId() { return id; }
    public String getNomFichier() { return nomFichier; }
    public String getCheminStockage() { return cheminStockage; }
    public String getTypeMime() { return typeMime; }
    public long getTailleOctets() { return tailleOctets; }
    public LocalDateTime getDateDepot() { return dateDepot; }
    public Utilisateur getAuteur() { return auteur; }
    public Long getMatiereId() { return matiereId; }
}
