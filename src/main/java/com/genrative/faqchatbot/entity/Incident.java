package com.genrative.faqchatbot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "incidents")
@Data
@NoArgsConstructor
public class Incident {
    @Id
    private String id;

    @Field("session_id")
    @Indexed
    private String sessionId;

    @Field("incident_id")
    @Indexed
    private String incidentId;

    @Field("priority")
    @Indexed
    private String priority;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("status")
    @Indexed
    private String status;

    public Incident(String sessionId, String incidentId, String priority) {
        this.sessionId = sessionId;
        this.incidentId = incidentId;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.status = "OPEN";
    }
}
