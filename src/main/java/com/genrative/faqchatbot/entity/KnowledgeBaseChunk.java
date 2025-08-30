package com.genrative.faqchatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kb_chunks")
@Data
@NoArgsConstructor
public class KnowledgeBaseChunk {
    @Id
    private String id;

    @Lob
    private String content;

    @Lob
    private String embeddings;

    @Lob
    private String metadata;
}
