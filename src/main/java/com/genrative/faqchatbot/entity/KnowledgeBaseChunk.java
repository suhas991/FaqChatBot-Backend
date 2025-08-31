package com.genrative.faqchatbot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "knowledge_base")
@Data
@NoArgsConstructor
public class KnowledgeBaseChunk {
    @Id
    private String id;

    @Field("content")
    @TextIndexed // MongoDB text search
    private String content;

    @Field("embedding")
    private List<Double> embedding; // Native array support

    @Field("metadata")
    private Map<String, Object> metadata;

    @Field("source_url")
    private String sourceUrl;

    @Field("chunk_index")
    private Integer chunkIndex;

    @Field("tags")
    @Indexed
    private List<String> tags;

    @Field("category")
    @Indexed
    private String category;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    public KnowledgeBaseChunk(String content, Map<String, Object> metadata) {
        this.content = content;
        this.metadata = metadata;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
