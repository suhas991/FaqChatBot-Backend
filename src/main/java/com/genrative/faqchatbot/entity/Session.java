package com.genrative.faqchatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name ="sessions")
@Data
@NoArgsConstructor
public class Session{

    @Id
    private String id;
    private String userId;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastActivity;

}
