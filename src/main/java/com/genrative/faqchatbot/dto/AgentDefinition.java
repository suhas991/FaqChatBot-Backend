package com.genrative.faqchatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentDefinition {
    private String role;
    private String goal;
    private String backstory;
    private Boolean reasoning;
    private String[] capabilities;
    private String[] limitations;
}
