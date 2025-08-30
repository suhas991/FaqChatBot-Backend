package com.genrative.faqchatbot.langchain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class RAGPipeline {
    public List<String> retriveRelevantChunks(String query,int k){
        log.debug("RAG retrieve called for query='{}' k={}", query, k);
        return Collections.emptyList();
    }
}
