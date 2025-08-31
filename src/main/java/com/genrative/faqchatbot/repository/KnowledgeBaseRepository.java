package com.genrative.faqchatbot.repository;

import com.genrative.faqchatbot.entity.KnowledgeBaseChunk;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KnowledgeBaseRepository extends MongoRepository<KnowledgeBaseChunk, String> {

    // Content contains (case insensitive) - This should work without text index
    @Query("{'content': {$regex: ?0, $options: 'i'}}")
    List<KnowledgeBaseChunk> findByContentContaining(String searchTerm);

    // MongoDB text search (requires text index)
    @Query("{ $text: { $search: ?0 } }")
    List<KnowledgeBaseChunk> findByContentText(String searchTerm);

    // Find by category
    List<KnowledgeBaseChunk> findByCategoryOrderByUpdatedAtDesc(String category);

    // Find all
    List<KnowledgeBaseChunk> findAllByOrderByUpdatedAtDesc();
}
