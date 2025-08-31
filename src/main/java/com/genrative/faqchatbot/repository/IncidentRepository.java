package com.genrative.faqchatbot.repository;

import com.genrative.faqchatbot.entity.Incident;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, String> {

    // Find incidents by session ID
    List<Incident> findBySessionIdOrderByCreatedAtDesc(String sessionId);

    // Find incidents by priority
    List<Incident> findByPriorityOrderByCreatedAtDesc(String priority);

    // Find by incident ID
    Incident findByIncidentId(String incidentId);

    // Find by status
    List<Incident> findByStatusOrderByCreatedAtDesc(String status);
}
