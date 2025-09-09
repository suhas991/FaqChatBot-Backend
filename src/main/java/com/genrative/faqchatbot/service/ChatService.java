package com.genrative.faqchatbot.service;

import com.genrative.faqchatbot.dto.ChatRequest;
import com.genrative.faqchatbot.dto.ChatResponse;
import com.genrative.faqchatbot.entity.Message;
import com.genrative.faqchatbot.entity.Session;
import com.genrative.faqchatbot.enums.SupportRole;
import com.genrative.faqchatbot.langchain.ChatModelProvider;
import com.genrative.faqchatbot.langchain.ConversationMemory;
import com.genrative.faqchatbot.langchain.RAGPipeline;
import com.genrative.faqchatbot.repository.MessageRepository;
import com.genrative.faqchatbot.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModelProvider chatModelProvider;
    private final ConversationMemory conversationMemory;
    private final RAGPipeline ragPipeline;
    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;

    public ChatResponse processMessage(ChatRequest request) {
        String sessionId = request.getSessionId();
        SupportRole role = request.getRole() != null ? request.getRole() : SupportRole.HELP_DESK;

        log.info("Processing message for session: {}", sessionId);

        // 1. Create or update session
        Session session = createOrUpdateSession(sessionId);

        // 2. Save user message
        Message userMessage = Message.createUserMessage(sessionId, request.getMessage());
        messageRepository.save(userMessage);

        // 3. Get conversation context
        List<Message> recent = conversationMemory.getRecentMessages(sessionId, 10);
        List<String> contexts = ragPipeline.retriveRelevantChunks(request.getMessage(), 5);

        // 4. Build prompt
        String knowledgeBase = formatKnowledgeBase(contexts);
        String conversationHistory = formatConversationHistory(recent);

        // 5. Generate AI response
        String reply = chatModelProvider.generateWithAgent(role,request.getMessage(),knowledgeBase,conversationHistory);

        // 6. Save bot message
        Message botMessage = Message.createBotMessage(sessionId, reply);
        messageRepository.save(botMessage);

        // 7. Update session statistics
        updateSessionStats(session);
        log.info("Generated response for session: {}", sessionId);

        return ChatResponse.builder()
                .reply(reply)
                .agentRole(role)
                .agentName(role.getDisplayname())
                .escalationSuggested(detectEscalationSuggestion(reply))
                .reasoning(String.valueOf(role.getAgentDefinition().getReasoning()))
                .build();
    }

    private Boolean detectEscalationSuggestion(String reply) {
        String[] escalationKeyWords = {"escalate","contact","specialist","advanced","complex"};
        String lowerReply = reply.toLowerCase();

        for (String keyword: escalationKeyWords){
            if(lowerReply.contains(keyword)){
                return true;
            }
        }
        return false;
    }

    private String formatConversationHistory(List<Message> recent) {
        if(recent.isEmpty()){
            return "No previous conversation found";
        }

        StringBuilder history = new StringBuilder();
        recent.forEach(h -> history.append(String.format("%s : %s",h.getSender(),h.getContent())));
        return history.toString();
    }

    private String formatKnowledgeBase(List<String> contexts) {
        if(contexts.isEmpty()){
            return "No relevant knowledge base information found";
        }
        StringBuilder kb = new StringBuilder();
        for(int i=0;i<contexts.size();i++){
            kb.append(String.format("KB_%d: %s\n",i+1,contexts.get(i)));
        }
        return kb.toString();
    }

    public void resetSession(String sessionId) {
        log.info("Resetting session: {}", sessionId);

        conversationMemory.clearMemory(sessionId);
        messageRepository.deleteBySessionId(sessionId);
        sessionRepository.deleteById(sessionId);

        log.info("Session reset completed: {}", sessionId);
    }

    private Session createOrUpdateSession(String sessionId) {
        Optional<Session> existingSession = sessionRepository.findById(sessionId);

        if (existingSession.isPresent()) {
            Session session = existingSession.get();
            session.updateActivity();
            return sessionRepository.save(session);
        } else {
            Session newSession = new Session(sessionId);
            return sessionRepository.save(newSession);
        }
    }

    private void updateSessionStats(Session session) {
        long messageCount = messageRepository.countBySessionId(session.getId());
        session.setTotalMessages((int) messageCount);
        session.updateActivity();
        sessionRepository.save(session);
    }


}
