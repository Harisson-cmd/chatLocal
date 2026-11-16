package com.chatlocal.chatlocal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.chatlocal.chatlocal.entity.*;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

/** Chaque test est annulé à la fin (rollback) : la base de test reste propre. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // vraie base PostgreSQL de test
@ActiveProfiles("test")
class PersistanceTest {

    @Autowired UtilisateurRepository utilisateurs;
    @Autowired ConversationRepository conversations;
    @Autowired MessageRepository messages;
    @Autowired DocumentRepository documents;
    JeuDeDonnees d;

    @BeforeEach
    void preparer() { d = new JeuDeDonnees(utilisateurs); }

    @Test
    void retrouveUnUtilisateurParIdentifiantEtGardeSonType() {
        assertThat(utilisateurs.findByIdentifiant("alice")).containsInstanceOf(Etudiant.class);
        Enseignant prof = (Enseignant) utilisateurs.findByIdentifiant("prof1").orElseThrow();
        assertThat(prof.isEstDelegue()).isTrue();
        assertThat(prof.getStatut()).isEqualTo(StatutCompte.ACTIF);
    }

    @Test
    void refuseDeuxUtilisateursAvecLeMemeIdentifiant() {
        Etudiant doublon = new Etudiant();
        doublon.setIdentifiant("alice");
        doublon.setMotDePasse("x");
        doublon.setNom("Autre");
        assertThatThrownBy(() -> utilisateurs.saveAndFlush(doublon))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void uneSeuleConversationParPaireQuelQueSoitLOrdre() {
        // on donne volontairement bob puis alice : la clé est la même dans les deux sens
        Conversation c = conversations.saveAndFlush(new ConversationPrivee(d.bob, d.alice));
        String cle = ConversationPrivee.cle(d.alice.getId(), d.bob.getId());
        assertThat(conversations.findByClePrivee(cle)).isPresent();
        assertThat(c.getParticipants()).hasSize(2);
        assertThatThrownBy(() -> conversations.saveAndFlush(new ConversationPrivee(d.alice, d.bob)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void listeLesConversationsDUnUtilisateur() {
        conversations.saveAndFlush(new ConversationPrivee(d.alice, d.bob));
        conversations.saveAndFlush(new ConversationPrivee(d.alice, d.prof));
        assertThat(conversations.toutesDe(d.alice.getId())).hasSize(2);
        assertThat(conversations.toutesDe(d.bob.getId())).hasSize(1);
        assertThat(conversations.toutesDe(d.admin.getId())).isEmpty();
    }

    @Test
    void refuseUnMessageAvecLeMemeIdClient() {
        Conversation c = conversations.save(new ConversationPrivee(d.alice, d.bob));
        String uuid = UUID.randomUUID().toString();
        messages.saveAndFlush(new Message(uuid, c, d.alice, "Bonjour", null));
        assertThat(messages.existsByIdClient(uuid)).isTrue();
        assertThatThrownBy(() -> messages.saveAndFlush(new Message(uuid, c, d.alice, "Bonjour", null)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void historiqueDuPlusRecentAuPlusAncienParPages() {
        Conversation c = conversations.save(new ConversationPrivee(d.alice, d.bob));
        for (int i = 1; i <= 5; i++)
            messages.save(new Message(UUID.randomUUID().toString(), c, d.alice, "msg " + i, null));
        Page<Message> page0 = messages.findByConversationIdOrderByIdDesc(c.getId(), PageRequest.of(0, 2));
        assertThat(page0.getContent()).extracting(Message::getContenu).containsExactly("msg 5", "msg 4");
        assertThat(page0.getTotalElements()).isEqualTo(5);
    }

    @Test
    void messageAvecDocumentJoint() {
        Conversation c = conversations.save(new ConversationPrivee(d.alice, d.bob));
        Document doc = documents.save(new Document("TD1.pdf", "uploads/" + UUID.randomUUID() + ".pdf", "application/pdf", 2048, d.prof));
        Message m = messages.saveAndFlush(new Message(UUID.randomUUID().toString(), c, d.alice, null, doc));
        assertThat(m.getDocument().getId()).isEqualTo(doc.getId());
        assertThat(doc.getMatiereId()).isNull();
    }
}
