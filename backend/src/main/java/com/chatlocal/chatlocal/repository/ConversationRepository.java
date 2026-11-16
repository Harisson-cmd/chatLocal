package com.chatlocal.chatlocal.repository;

import com.chatlocal.chatlocal.entity.Conversation;
import com.chatlocal.chatlocal.entity.ConversationPrivee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /** Conversation privée entre deux utilisateurs : passer ConversationPrivee.cle(idA, idB). */
    Optional<ConversationPrivee> findByClePrivee(String clePrivee);

    /** Conversations privées d'un utilisateur, la plus récente d'abord. */
    @Query("select c from ConversationPrivee c join c.participants p where p.id = :uid "
         + "order by c.dateDerniereActivite desc")
    List<ConversationPrivee> toutesDe(@Param("uid") Long utilisateurId);
}
