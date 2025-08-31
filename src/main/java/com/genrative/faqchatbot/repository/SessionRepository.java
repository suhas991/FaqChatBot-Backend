package com.genrative.faqchatbot.repository;

import com.genrative.faqchatbot.entity.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

    // Find sessions by user ID
    List<Session> findByUserIdOrderByLastActivityDesc(String userId);

    // Find active sessions (last activity within specified time)
    @Query("{'lastActivity': {$gte: ?0}}")
    List<Session> findActiveSessions(LocalDateTime since);

    // Find session by ID
    Optional<Session> findById(String id);

    // Count total sessions
    long count();

    // Delete old sessions
    @Query(value = "{'lastActivity': {$lt: ?0}}", delete = true)
    void deleteOldSessions(LocalDateTime before);
}
