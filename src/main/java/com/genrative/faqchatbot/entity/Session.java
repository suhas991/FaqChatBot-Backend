package com.genrative.faqchatbot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Document(collection = "sessions")
@Data
@NoArgsConstructor
public class Session{

    @Id
    private String id;

    @Field("user_id")
    @Indexed
    private String userId;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("last_activity")
    @Indexed
    private LocalDateTime lastActivity;

    @Field("total_messages")
    private Integer totalMessages = 0;

    public Session(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
        this.totalMessages = 0;
    }

    public void updateActivity() {
        this.lastActivity = LocalDateTime.now();
    }

}
