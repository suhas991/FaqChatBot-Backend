package com.genrative.faqchatbot.langchain;

import com.genrative.faqchatbot.dto.IncidentRequest;
import com.genrative.faqchatbot.dto.ServiceNowIncidentPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncidentSummarizer {
    private final ChatModelProvider modelProvider;

    public ServiceNowIncidentPayload summarizeForIncident(String transcript, IncidentRequest form){
        String prompt = """
                You are an assistant that converts a customer support conversation into a ServiceNow incident payload.
                Produce a short one-line summary and a detailed description.

                Conversation:
                %s

                Produce output as:
                SHORT: <one line>
                LONG: <detailed description>
                """.formatted(transcript);

        String gen = modelProvider.generate(prompt);
        String shortDesc = form.getIssueSummary();
        String longDesc = form.getIssueDetails();

        if (gen != null && !gen.isBlank()) {
            // Very simple parse attempt
            int idxShort = gen.indexOf("SHORT:");
            int idxLong = gen.indexOf("LONG:");
            try {
                if (idxShort >= 0 && idxLong > idxShort) {
                    shortDesc = gen.substring(idxShort + 6, idxLong).trim();
                    longDesc = gen.substring(idxLong + 5).trim();
                } else if (gen.length() < 200) {
                    shortDesc = gen.trim();
                    longDesc = gen.trim();
                } else {
                    // fallback
                    shortDesc = gen.lines().findFirst().orElse(form.getIssueSummary());
                    longDesc = gen;
                }
            } catch (Exception e) {
                shortDesc = form.getIssueSummary();
                longDesc = form.getIssueDetails();
            }
        }

        ServiceNowIncidentPayload payload = new ServiceNowIncidentPayload();
        payload.setShort_description(shortDesc);
        payload.setDescription(longDesc);
        payload.setCategory("inquiry");
        // payload.setUrgency(form.getPriority() != null ? form.getPriority() : "3"); // default medium
        payload.setCaller_id(form.getUserId());
        return payload;


    }
}
