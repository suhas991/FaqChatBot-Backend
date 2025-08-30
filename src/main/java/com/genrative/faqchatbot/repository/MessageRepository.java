package com.genrative.faqchatbot.repository;

import com.genrative.faqchatbot.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,String> {

    List<Message> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    void deleteBySessionId(String sessionId);

    @Query(value = "SELECT m FROM Message m WHERE m.sessionId = :sessionId ORDER BY m.createdAt DESC")
    List<Message> findTopNBySessionIdOrderByCreatedAtDesc(String sessionId, org.springframework.data.domain.Pageable pageable);

    default List<Message> findTopNBySessionIdOrderByCreatedAtDesc(String sessionId, int n) {
        return findTopNBySessionIdOrderByCreatedAtDesc(sessionId, org.springframework.data.domain.PageRequest.of(0, n));
    }
}
