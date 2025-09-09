package com.genrative.faqchatbot.langchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genrative.faqchatbot.dto.AgentDefinition;
import com.genrative.faqchatbot.enums.SupportRole;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Getter
@Component
@Slf4j
public class ChatModelProvider {
    private final ChatModel model;
    private final ObjectMapper objectMapper;

    public ChatModelProvider(){
        String apiKey = "AIzaSyDccWRnMUGycEv8vaG9ggu5FXZpLtEzlFI";
        if(apiKey.isBlank()){
            throw new IllegalStateException("API KEY IS MISSING");
        }

        model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-1.5-flash")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String generate(String prompt){
        return model.chat(prompt);
    }

    public String generateWithAgent(SupportRole role,String userQuery,String knowledgeBase,String conversationHistory){
        try{
            AgentDefinition agent = role.getAgentDefinition();
            String agentJson = objectMapper.writeValueAsString(agent);

            String structuredPrompt = buildStructuredPrompt(agentJson,userQuery,knowledgeBase,conversationHistory);
            log.info("Generating response with agent: {}",agent.getRole());

            return model.chat(structuredPrompt);

        }catch (JsonProcessingException exe){
            log.error("Error serializing agent definition..!");
            return generate(userQuery);
        }
    }

    private String buildStructuredPrompt(String agentJson, String userQuery, String knowledgeBase, String conversationHistory) {
        return String.format("""
            You are an AI assistant acting as a specialized IT support agent. Below is your agent configuration in JSON format:

            AGENT_CONFIGURATION:
            %s

            INSTRUCTIONS:
            1. Embody the role, goal, and backstory defined in your agent configuration
            2. Use your specified capabilities to help the user
            3. Acknowledge your limitations and suggest escalation when appropriate
            4. If reasoning is enabled, think through the problem step-by-step before responding
            5. Maintain the personality and expertise level defined in your backstory
            6. Provide practical, actionable advice based on your role

            KNOWLEDGE_BASE:
            %s

            CONVERSATION_HISTORY:
            %s

            USER_QUERY: %s

            RESPONSE_FORMAT:
            - If reasoning is enabled, first provide your thought process briefly
            - Then provide a clear, helpful response based on your role
            - Include specific steps or commands when appropriate
            - Mention if escalation is needed based on your limitations
            - Stay in character as the specified IT support role

            Your response:
            """, agentJson, knowledgeBase, conversationHistory, userQuery);
    }
}
