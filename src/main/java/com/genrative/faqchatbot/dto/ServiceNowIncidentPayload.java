package com.genrative.faqchatbot.dto;

import lombok.Data;

@Data
public class ServiceNowIncidentPayload {
    private String short_description;
    private String description;
    private String category;
    private String urgency;
    private String caller_id;
}