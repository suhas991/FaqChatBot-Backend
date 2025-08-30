package com.genrative.faqchatbot.langchain;

import com.genrative.faqchatbot.entity.Message;
import com.genrative.faqchatbot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConversationMemory {
    private final MessageRepository messageRepository;

    public List<Message> getRecentMessages(String sessionId,int limit){
        return messageRepository.findTopNBySessionIdOrderByCreatedAtDesc(sessionId,limit);
    }

    public void clearMemory(String sessionId){
        // lets handle later
    }
}
