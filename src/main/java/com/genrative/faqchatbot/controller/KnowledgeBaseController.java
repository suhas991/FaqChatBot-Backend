package com.genrative.faqchatbot.controller;

import com.genrative.faqchatbot.dto.KnowledgeRequest;
import com.genrative.faqchatbot.entity.KnowledgeBaseChunk;
import com.genrative.faqchatbot.service.KnowledgeBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge-base")
@RequiredArgsConstructor
public class KnowledgeBaseController {

    private final KnowledgeBaseService kbService;

    @PostMapping("/populate-sample")
    public ResponseEntity<String> populateSampleData() {
        kbService.populateSampleKnowledgeBase();
        return ResponseEntity.ok("Sample knowledge base populated successfully");
    }

    @PostMapping("/add")
    public ResponseEntity<KnowledgeBaseChunk> addKnowledge(@RequestBody KnowledgeRequest request) {
        KnowledgeBaseChunk chunk = kbService.addKnowledgeChunk(
                request.getContent(),
                request.getMetadata() != null ? request.getMetadata() : Map.of()
        );
        return ResponseEntity.ok(chunk);
    }

    @GetMapping("/search")
    public ResponseEntity<List<KnowledgeBaseChunk>> searchKnowledge(@RequestParam String query) {
        List<KnowledgeBaseChunk> results = kbService.searchKnowledge(query);
        return ResponseEntity.ok(results);
    }
}

