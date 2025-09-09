package com.genrative.faqchatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genrative.faqchatbot.enums.SupportRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatResponse{
    private String reply;
    private SupportRole agentRole;
    private String agentName;
    private Boolean escalationSuggested;
    private String reasoning;

    // Keep backward compatibility for older version support
    public ChatResponse(String reply){
        this.reply = reply;
    }

}
