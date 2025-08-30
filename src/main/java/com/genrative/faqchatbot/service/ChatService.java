package com.genrative.faqchatbot.service;

import com.genrative.faqchatbot.dto.ChatRequest;
import com.genrative.faqchatbot.dto.ChatResponse;
import com.genrative.faqchatbot.entity.Message;
import com.genrative.faqchatbot.langchain.ChatModelProvider;
import com.genrative.faqchatbot.langchain.ConversationMemory;
import com.genrative.faqchatbot.langchain.RAGPipeline;
import com.genrative.faqchatbot.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModelProvider chatModelProvider;
    private final ConversationMemory conversationMemory;
    private final RAGPipeline ragPipeline;
    private final MessageRepository messageRepository;

    @Transactional
    public ChatResponse processMessage(ChatRequest request){
        String sessionId = request.getSessionId();

        Message usermsg = Message.builder()
                .sessionId(sessionId)
                .sender("USER")
                .content(request.getMessage())
                .createdAt(OffsetDateTime.now())
                .build();
        messageRepository.save(usermsg);

        List<Message> recent = conversationMemory.getRecentMessages(sessionId,10);
        List<String> contexts = ragPipeline.retriveRelevantChunks(request.getMessage(),5);

        StringBuilder prompt = new StringBuilder();
        prompt.append("Conversation so far:\n");
        recent.forEach(m -> prompt.append(m.getSender()).append(": ").append(m.getContent()).append("\n"));
        prompt.append("\nContext:\n");
        contexts.forEach(c -> prompt.append("- ").append(c).append("\n"));
        prompt.append("\nUser: ").append(request.getMessage()).append("\nAssistant:");

        String reply = chatModelProvider.generate(prompt.toString());

        Message botMsg = Message.builder()
                .sessionId(sessionId)
                .sender("BOT")
                .content(reply)
                .createdAt(OffsetDateTime.now())
                .build();
        messageRepository.save(botMsg);

        return new ChatResponse(reply);
    }

    @Transactional
    public void resetSession(String sessionId) {
        conversationMemory.clearMemory(sessionId);
        // Optionally remove persisted messages for dev
        messageRepository.deleteBySessionId(sessionId);
    }
}
