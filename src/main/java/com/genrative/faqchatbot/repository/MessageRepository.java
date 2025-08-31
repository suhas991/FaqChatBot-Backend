package com.genrative.faqchatbot.repository;

import com.genrative.faqchatbot.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    // Find messages by session ID ordered by creation time
    List<Message> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    List<Message> findBySessionIdOrderByCreatedAtDesc(String sessionId);

    // Delete all messages for a session
    void deleteBySessionId(String sessionId);

    // Find top N messages by session ID
    @Query("{'sessionId': ?0}")
    List<Message> findTopNBySessionIdOrderByCreatedAtDesc(String sessionId, Pageable pageable);

    // Default method for convenience
    default List<Message> findTopNBySessionIdOrderByCreatedAtDesc(String sessionId, int n) {
        return findTopNBySessionIdOrderByCreatedAtDesc(
                sessionId,
                Pageable.ofSize(n)
        );
    }

    // Count messages in a session
    long countBySessionId(String sessionId);

    // Find messages by sender
    List<Message> findBySessionIdAndSender(String sessionId, String sender);

    // Find recent messages across all sessions
    @Query("{'createdAt': {$gte: ?0}}")
    List<Message> findRecentMessages(LocalDateTime since);

    // Text search in message content
    @Query("{'content': {$regex: ?0, $options: 'i'}}")
    List<Message> findByContentContaining(String searchTerm);
}
