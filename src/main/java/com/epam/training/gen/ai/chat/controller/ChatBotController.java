package com.epam.training.gen.ai.chat.controller;

import com.epam.training.gen.ai.chat.model.PromptResponse;
import com.epam.training.gen.ai.chat.prompt.PromptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ChatBotController {

    private final PromptService promptService;

    public ChatBotController(PromptService promptService){
        this.promptService = promptService;
    }

    @GetMapping("/api/prompt")
    public Map<String, Object> prompt(@RequestParam(required = false) String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be empty");
        }

        PromptResponse response = generateResponse(prompt);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("prompt", prompt);
        responseMap.put("response", response);

        return responseMap;
    }

    @GetMapping("/api/chat")
    public Map<String, Object> chat(@RequestParam(required = false) String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be empty");
        }

        PromptResponse response = generateResponse(prompt);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("prompt", prompt);
        responseMap.put("response", response);

        return responseMap;
    }

    private PromptResponse generateResponse(String prompt)  {
        return Optional.ofNullable(promptService)
                .map(promptService -> promptService.getChatBotResponse(prompt))
                .orElseGet(PromptResponse::new);

    }
}