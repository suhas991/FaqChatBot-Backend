package com.genrative.faqchatbot.langchain;

import com.genrative.faqchatbot.entity.KnowledgeBaseChunk;
import com.genrative.faqchatbot.repository.KnowledgeBaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor  // ✅ This creates constructor injection
public class RAGPipeline {

    private final KnowledgeBaseRepository kbRepository; // ✅ This will be injected

    public List<String> retriveRelevantChunks(String query, int k) {
        log.info("🔍 Searching KB for query: '{}'", query);

        try {
            // Try content containing search first (more reliable)
            List<KnowledgeBaseChunk> kbResults = kbRepository.findByContentContaining(query);

            log.info("📊 Found {} KB chunks for query: '{}'", kbResults.size(), query);

            if (kbResults.isEmpty()) {
                log.warn("⚠️ No KB results found for query: '{}'", query);
                return Collections.emptyList();
            }

            // Log what we found (for debugging)
            kbResults.forEach(chunk ->
                    log.debug("📋 KB Chunk: {}",
                            chunk.getContent().substring(0, Math.min(100, chunk.getContent().length())) + "...")
            );

            return kbResults.stream()
                    .map(KnowledgeBaseChunk::getContent)
                    .limit(k)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("❌ Error searching KB: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
