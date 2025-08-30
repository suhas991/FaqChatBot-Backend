package com.genrative.faqchatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentRequest {
    private String sessionId;
    private String userId;
    private String userName;
    private String issueSummary;
    private String issueDetails;
}
