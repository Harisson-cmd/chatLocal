package com.chatlocal.chatlocal.repository;

import com.chatlocal.chatlocal.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /** Historique du plus récent au plus ancien (page 0 = les derniers messages). */
    Page<Message> findByConversationIdOrderByIdDesc(Long conversationId, Pageable page);

    /** Sert à ignorer un message renvoyé deux fois. */
    Optional<Message> findByIdClient(String idClient);

    boolean existsByIdClient(String idClient);
}
