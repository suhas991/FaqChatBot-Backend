package com.genrative.faqchatbot.service;

import com.genrative.faqchatbot.entity.KnowledgeBaseChunk;
import com.genrative.faqchatbot.repository.KnowledgeBaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeBaseService {

    private final KnowledgeBaseRepository kbRepository;

    public KnowledgeBaseChunk addKnowledgeChunk(String content, Map<String, Object> metadata) {
        KnowledgeBaseChunk chunk = new KnowledgeBaseChunk(content, metadata);
        chunk.setId(UUID.randomUUID().toString());

        if (metadata.containsKey("category")) {
            chunk.setCategory((String) metadata.get("category"));
        }

        return kbRepository.save(chunk);
    }

    public void populateSampleKnowledgeBase() {
        log.info("Populating role-specific sample knowledge base...");

        List<Map<String, Object>> sampleData = Arrays.asList(
                // Help Desk / Service Desk Support
                Map.of(
                        "content", "Password Reset Procedure: 1. Go to login page 2. Click 'Forgot Password' 3. Enter email address 4. Check email for reset link 5. Click link and create new password 6. Confirm new password works",
                        "category", "help_desk",
                        "role", "HELP_DESK",
                        "tags", Arrays.asList("password", "reset", "login", "account"),
                        "priority", "high"
                ),
                Map.of(
                        "content", "Basic Email Setup: 1. Open email client 2. Select 'Add Account' 3. Enter email address 4. Use auto-configure if available 5. If manual setup needed: IMAP server: mail.company.com, Port 993 6. Test send/receive",
                        "category", "help_desk",
                        "role", "HELP_DESK",
                        "tags", Arrays.asList("email", "setup", "configuration"),
                        "priority", "medium"
                ),
                Map.of(
                        "content", "Printer Not Working: 1. Check power and cables 2. Check paper and toner 3. Restart printer 4. Check print queue on computer 5. Try printing test page 6. If still not working, contact technical support",
                        "category", "help_desk",
                        "role", "HELP_DESK",
                        "tags", Arrays.asList("printer", "printing", "hardware"),
                        "priority", "medium"
                ),

                // Technical Troubleshooting
                Map.of(
                        "content", "Network Connectivity Troubleshooting: 1. Check physical connections 2. Run ipconfig /release && ipconfig /renew 3. Test with ping 8.8.8.8 4. Check DNS with nslookup google.com 5. Test different websites 6. Check proxy settings 7. Restart network adapter",
                        "category", "technical",
                        "role", "TECHNICAL_TROUBLESHOOTING",
                        "tags", Arrays.asList("network", "connectivity", "ping", "dns"),
                        "priority", "high"
                ),
                Map.of(
                        "content", "Windows Blue Screen (BSOD) Troubleshooting: 1. Note the error code 2. Boot in Safe Mode 3. Check Event Viewer for errors 4. Run memory diagnostic (mdsched.exe) 5. Check hard drive with chkdsk 6. Update or rollback drivers 7. Run system file checker (sfc /scannow)",
                        "category", "technical",
                        "role", "TECHNICAL_TROUBLESHOOTING",
                        "tags", Arrays.asList("bsod", "crash", "windows", "diagnostic"),
                        "priority", "high"
                ),

                // System Administration
                Map.of(
                        "content", "User Account Management: 1. Access Active Directory Users and Computers 2. Navigate to appropriate OU 3. Right-click to create new user 4. Set username, password, and initial groups 5. Configure password policies 6. Test account access 7. Document changes",
                        "category", "system_admin",
                        "role", "SYSTEM_ADMINISTRATION",
                        "tags", Arrays.asList("user", "account", "active directory", "administration"),
                        "priority", "high"
                ),
                Map.of(
                        "content", "Group Policy Management: 1. Open Group Policy Management Console 2. Navigate to appropriate OU 3. Create or edit GPO 4. Configure required settings 5. Link GPO to OU 6. Test policy application 7. Document policy changes",
                        "category", "system_admin",
                        "role", "SYSTEM_ADMINISTRATION",
                        "tags", Arrays.asList("group policy", "gpo", "policy", "administration"),
                        "priority", "medium"
                ),

                // Infrastructure Management
                Map.of(
                        "content", "Server Health Monitoring: 1. Check CPU utilization (should be <80%) 2. Monitor memory usage 3. Check disk space (alert if >85% full) 4. Review system logs for errors 5. Verify backup completion 6. Test critical services 7. Update monitoring dashboards",
                        "category", "infrastructure",
                        "role", "INFRASTRUCTURE_MANAGEMENT",
                        "tags", Arrays.asList("server", "monitoring", "performance", "health"),
                        "priority", "high"
                ),
                Map.of(
                        "content", "Disaster Recovery Planning: 1. Identify critical systems and data 2. Define RTO and RPO requirements 3. Document backup and restore procedures 4. Test recovery procedures regularly 5. Maintain offsite backup copies 6. Review and update DR plan quarterly",
                        "category", "infrastructure",
                        "role", "INFRASTRUCTURE_MANAGEMENT",
                        "tags", Arrays.asList("disaster recovery", "backup", "business continuity"),
                        "priority", "critical"
                )
        );

        for (Map<String, Object> data : sampleData) {
            String content = (String) data.get("content");
            addKnowledgeChunk(content, data);
        }

        log.info("Role-specific knowledge base populated with {} entries", sampleData.size());
    }

    public List<KnowledgeBaseChunk> searchKnowledge(String query) {
        // First try text search
        List<KnowledgeBaseChunk> results = kbRepository.findByContentText(query);

        // If no results, try content contains
        if (results.isEmpty()) {
            results = kbRepository.findByContentContaining(query);
        }

        return results;
    }
}

