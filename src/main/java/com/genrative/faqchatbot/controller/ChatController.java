package com.genrative.faqchatbot.controller;

import com.genrative.faqchatbot.dto.ChatRequest;
import com.genrative.faqchatbot.dto.ChatResponse;
import com.genrative.faqchatbot.service.ChatService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public String healthCheck(){
       return "Server is up and running..!";
    }

    @PostMapping
    public ResponseEntity<ChatResponse> handleChat(@RequestBody ChatRequest request){
        if(request.getSessionId() == null || request.getSessionId().isBlank()){
            throw new RuntimeException("Session Id is Required");
        }
        ChatResponse res = chatService.processMessage(request);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/reset/{sessionId}")
    public ResponseEntity<String> resetSession(@PathVariable String sessionId) {
        chatService.resetSession(sessionId);
        return ResponseEntity.ok("Session " + sessionId + " cleared.");
    }
}
