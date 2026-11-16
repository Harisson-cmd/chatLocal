package com.chatlocal.chatlocal.repository;

import com.chatlocal.chatlocal.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    /** Bibliothèque commune paginée, les plus récents d'abord. */
    Page<Document> findAllByOrderByDateDepotDesc(Pageable page);
}
