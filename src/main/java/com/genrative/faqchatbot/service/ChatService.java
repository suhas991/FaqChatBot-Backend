package com.genrative.faqchatbot.service;

import com.genrative.faqchatbot.dto.ChatRequest;
import com.genrative.faqchatbot.dto.ChatResponse;
import com.genrative.faqchatbot.entity.Message;
import com.genrative.faqchatbot.entity.Session;
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
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a helpful customer support assistant.\n");
        prompt.append("Use the knowledge base information to answer questions when relevant.\n\n");
        prompt.append("Knowledge Base Information:\n");
        if (contexts.isEmpty()) {
            prompt.append("- No relevant knowledge base information found\n");
        } else {
            contexts.forEach(c -> prompt.append("- ").append(c).append("\n"));
        }
        prompt.append("\nConversation History:\n");
        recent.forEach(m -> prompt.append(m.getSender()).append(": ").append(m.getContent()).append("\n"));
        prompt.append("\nUser Question: ").append(request.getMessage());
        prompt.append("\n\nAssistant: Please provide a helpful answer based on the knowledge base information above. If no relevant information is available, provide a general helpful response.");

        // 5. Generate AI response
        String reply = chatModelProvider.generate(prompt.toString());

        // 6. Save bot message
        Message botMessage = Message.createBotMessage(sessionId, reply);
        messageRepository.save(botMessage);

        // 7. Update session statistics
        updateSessionStats(session);

        log.info("Generated response for session: {}", sessionId);

        return new ChatResponse(reply);
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
