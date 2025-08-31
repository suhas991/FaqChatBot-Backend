package com.genrative.faqchatbot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "messages")
@CompoundIndex(name = "session_created_idx", def = "{'sessionId': 1, 'createdAt': -1}")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    private String id;

    @Field("session_id")
    @Indexed
    private String sessionId;

    @Field("sender")
    @Indexed
    private String sender;

    @Field("content")
    private String content;

    @Field("created_at")
    @Indexed
    private LocalDateTime createdAt;

    @Field("processed")
    private Boolean processed = false;

    public static Message createUserMessage(String sessionId, String content) {
        return Message.builder()
                .sessionId(sessionId)
                .sender("USER")
                .content(content)
                .createdAt(LocalDateTime.now())
                .processed(false)
                .build();
    }

    public static Message createBotMessage(String sessionId, String content) {
        return Message.builder()
                .sessionId(sessionId)
                .sender("BOT")
                .content(content)
                .createdAt(LocalDateTime.now())
                .processed(true)
                .build();
    }
}

