package com.genrative.faqchatbot.langchain;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ChatModelProvider {
    private final ChatModel model;

    public ChatModelProvider(){
        String apiKey = "AIzaSyDccWRnMUGycEv8vaG9ggu5FXZpLtEzlFI";
        if(apiKey.isBlank()){
            throw new IllegalStateException("API KEY IS MISSING");
        }

        model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-1.5-flash")
                .build();

    }

    public String generate(String prompt){
        return model.chat(prompt);
    }
}
