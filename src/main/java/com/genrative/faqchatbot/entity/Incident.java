package com.genrative.faqchatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@Table(name="incidents")
@NoArgsConstructor
public class Incident {

    @Id
    private String id;
    private String sessionId;
    private String incidentId;
    private String priority;
    private OffsetDateTime createdAt;

}
