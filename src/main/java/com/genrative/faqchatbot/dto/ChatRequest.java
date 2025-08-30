package com.genrative.faqchatbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {
    private String sessionId;
    private String message;
}
