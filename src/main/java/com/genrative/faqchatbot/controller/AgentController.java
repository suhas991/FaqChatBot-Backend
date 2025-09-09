package com.genrative.faqchatbot.controller;

import com.genrative.faqchatbot.dto.AgentDefinition;
import com.genrative.faqchatbot.enums.SupportRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    @GetMapping("roles")
    public ResponseEntity<List<Map<String,Object>>> getAvailableRoles() {
        List<Map<String, Object>> roles = Arrays.stream(SupportRole.values())
                .map(role -> {
                    Map<String, Object> map = new HashMap<>();
                    AgentDefinition agent = role.getAgentDefinition();
                    map.put("key", role.name());
                    map.put("role", agent.getRole());
                    map.put("goal", agent.getGoal());
                    map.put("capabilities", agent.getCapabilities());
                    map.put("limitations", agent.getLimitations());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/roles/{roleKey}")
    public ResponseEntity<AgentDefinition> getRoleDefinition(@PathVariable String roleKey) {
        try {
            SupportRole role = SupportRole.valueOf(roleKey.toUpperCase());
            return ResponseEntity.ok(role.getAgentDefinition());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
