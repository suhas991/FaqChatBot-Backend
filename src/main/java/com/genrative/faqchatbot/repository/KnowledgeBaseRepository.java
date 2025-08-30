package com.genrative.faqchatbot.repository;

import com.genrative.faqchatbot.entity.KnowledgeBaseChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBaseChunk,String> {

}
