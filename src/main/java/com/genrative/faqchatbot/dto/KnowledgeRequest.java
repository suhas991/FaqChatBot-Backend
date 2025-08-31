package com.genrative.faqchatbot.dto;

import lombok.Data;

import java.util.Map;

@Data
public class KnowledgeRequest {
    private String content;
    private Map<String, Object> metadata;
}
