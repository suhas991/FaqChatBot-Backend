package com.genrative.faqchatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentResponse {
    private String incidentId;
    private String url;
    private String status;
}
