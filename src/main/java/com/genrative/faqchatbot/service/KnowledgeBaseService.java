package com.genrative.faqchatbot.service;

import com.genrative.faqchatbot.entity.KnowledgeBaseChunk;
import com.genrative.faqchatbot.repository.KnowledgeBaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeBaseService {

    private final KnowledgeBaseRepository kbRepository;

    public KnowledgeBaseChunk addKnowledgeChunk(String content, Map<String, Object> metadata) {
        KnowledgeBaseChunk chunk = new KnowledgeBaseChunk(content, metadata);
        chunk.setId(UUID.randomUUID().toString());

        if (metadata.containsKey("category")) {
            chunk.setCategory((String) metadata.get("category"));
        }

        return kbRepository.save(chunk);
    }

    public void populateSampleKnowledgeBase() {
        log.info("Populating sample knowledge base...");

        List<Map<String, Object>> sampleData = Arrays.asList(
                Map.of(
                        "content", "How to reset password: Go to login page, click forgot password, enter your email address and follow the instructions sent to your email.",
                        "category", "authentication",
                        "tags", Arrays.asList("password", "reset", "login"),
                        "priority", "high"
                ),
                Map.of(
                        "content", "How to contact support: You can reach our support team by calling 1-800-SUPPORT or sending an email to support@company.com. Our hours are Monday to Friday 9AM-6PM EST.",
                        "category", "support",
                        "tags", Arrays.asList("contact", "support", "phone", "email"),
                        "priority", "high"
                ),
                Map.of(
                        "content", "Working hours: Our customer service team is available Monday through Friday from 9:00 AM to 6:00 PM Eastern Standard Time. We are closed on weekends and major holidays.",
                        "category", "general",
                        "tags", Arrays.asList("hours", "availability", "schedule"),
                        "priority", "medium"
                ),
                Map.of(
                        "content", "How to update profile: Login to your account, navigate to Settings menu, click on Profile section, edit your information and click Save Changes to update.",
                        "category", "account",
                        "tags", Arrays.asList("profile", "update", "settings"),
                        "priority", "medium"
                ),
                Map.of(
                        "content", "How to cancel subscription: Login to your account, go to Account Settings, select Subscription Management, click Cancel Subscription and confirm your decision.",
                        "category", "billing",
                        "tags", Arrays.asList("cancel", "subscription", "billing"),
                        "priority", "high"
                )
        );

        for (Map<String, Object> data : sampleData) {
            String content = (String) data.get("content");
            addKnowledgeChunk(content, data);
        }

        log.info("Sample knowledge base populated with {} entries", sampleData.size());
    }

    public List<KnowledgeBaseChunk> searchKnowledge(String query) {
        // First try text search
        List<KnowledgeBaseChunk> results = kbRepository.findByContentText(query);

        // If no results, try content contains
        if (results.isEmpty()) {
            results = kbRepository.findByContentContaining(query);
        }

        return results;
    }
}

